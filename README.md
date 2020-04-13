# kotlin
parse interface use create view could create view
it support databinding,anko and normal way to create view
you could read the demo and build project what you want 

```
def binding_version='2.1.2'
implementation "com.lifecycle:binding:$binding_version"
implementation "com.lifecycle:rx:$binding_version"
implementation "com.lifecycle:coroutines:$binding_version"
```

从两三年前，就一直想介绍一下自己在实战的项目结构<br>
地址：[https://github.com/striveprince/kotlin](https://github.com/striveprince/kotlin)

还记得我最初写的时候。android官方的lifecycle，viewmodel和liveData都还没有发布，<br>
从去年开始，我将之前的项目架构重新用kotlin重写了，同时引入databinding,dagger,retrofit,rxjava,协程等，最后将rx和协程拆开，好了，先看下面项目结构吧

首先现在有Parse和Inflate两个接口,都是可以解析@LayoutView的layout<br>
A-->B这个符号表示B是A的子类
```
Parse-->Normal
         -->AnkoParse 
         -->Binding-->DataBinding            -->DataBindInflate         -->DataBeanInflate
                   -->BindingActivity        -->DatabindingActivity
         -->BaseActivity-->NormalActivity
                        -->RecyclerActivity
                        -->AnkoActivity
                        -->BindingActivity-->DatabindingActivity
        -->BaseFragment(同上)
```
Parse是用于什么场景，可以看到这里有BaseActivity和BaseFragment,这里其实就是将解析视图和数据同时引入，就是有数据和界面两个类同时进行绑定<br>
Normal：LayoutInflater.from(context).inflate的方式解析<br>
AnkoParse：anko的方式解析<br>
Binding：viewBinding解析泛型类型加载<br>
DataBinding：databinding解析<br>
DataBindInflate：用于复用型View的解析<br>
BaseActivity：就是未定义解析方式的基本Activity,子类就是以上面几种方式解析的方式使用的方式<br>
BaseFragment：就是未定义解析方式的基本Fragment,同BaseActivity<br>
```
Inflate-->Diff                -->ErrorInflate
       -->BindingInflate      -->ViewBindInflate
                              -->ListViewInflate
       -->DataBindInflate     -->DataBeanInflate
       -->Select
```
Inflate：就是单纯的解析界面用于复用界面<br>
Diff:计算是否是同一个数据<br>
ErrorInflate：错误Inflate，可自定义<br>
BindingInflate：Databinding解析<br>
ViewBindInflate:BindingInflate的一个简单的实现类<br>
DataBindInflate：用于解析和界面分开，可以用于复用界面<br>
Select:将用于选择，配合RecyclerSelectAdapter<br>
DataBeanInflate:加入Attach和Detach，就是实现了attach和detach方法，另外还有Attach,Detach用于界面移入和移出时被调用（目前支持recyclerView）<br>
```
IEvent-->IList-->-->ListModel
                 -->ListAdapter
                 -->ListViewInflate
                 -->ISelect
                 -->FragmentOpenAdapter
                 -->WidgetOpenAdapter
                 -->FragmentOpenPager2Adapter
                 -->WidgetOpenAdapter
                 -->RecyclerOpenAdapter
```
IEvent：事件类，可以使用这个传入事件进行监听<br>
IList：复用界面事件类，是各种adapter的基类，可以实现列表的加载，刷新，删除，更新等操作<br>
ListModel：用于和adapter的数据类，可以使用adapter和数据结合的一个类，可以仔细看其子类ListViewModel<br>
ISelect：用于选择场景<br>
ListInflate：Inflate和IList结合的一个接口<br>
ListViewInflate：ListInflate的一个实现类<br>
由于以上很多都是的接口，所以很多都可以自由组合。比如ListInflate,DataBindInflate等<br>

 实现
 是Application中onCreate的中先注册AppLifecycle.这里实现了异步注册，在注册完成的时候，postInitFinish方法会发送注册完成通知其他界面，BaseActivity中的initData方法会等待注册postIntiFinish方法调用，具体逻辑可以看代码。<br>
先看相应的parse中的createview方法
```
interface Parse<T,B>{
    fun t():T
    /**
     * 解析layout创建View，这里是用的最简单的解析方式
     * */
    fun createView(t:T,context: Context, parent: ViewGroup?=null, attachToParent: Boolean=false): View {
        return LayoutInflater.from(context).inflate(findLayoutView(this.javaClass).layout[0],parent,attachToParent)
    }
    /**
     * 这里是解析出view的持有类型，如ViewDataBinding
     * */
    fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B
}
 
再看子类Binding
interface Binding<T,B:ViewBinding>:Parse<T, B> {
    /**
     * 覆盖了Parse中parse的方法，以反射的方式解析出ViewBinding
     * */
    @Suppress("UNCHECKED_CAST")
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        val clazz = javaClass.kotlin.supertypes[0].arguments[1].type!!.javaType as Class<B>
        val method = clazz.getMethod("inflate",LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
        return method.invoke(null,LayoutInflater.from(context),parent,attachToParent) as B
    }

    /**
     * 覆盖了Parse中createView的方法，调用了parse方法来获取root
     * */
    override fun createView(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        return parse(t,context,parent, attachToParent).root
    }
}
再看子类Binding
interface DataBinding<T, B : ViewDataBinding> : Binding<T, B> {
    /**
     * 这里又覆盖了Binding的parse方法，以DataBindingUtil.inflate的方式加载
     * */
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return runCatching {
            (DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, attachToParent) as B)
                .apply {
                    setVariable(appLifecycle.vm, t)
                    setVariable(appLifecycle.parse, this@DataBinding)
                } }
            .getOrElse { super.parse(t, context, parent, attachToParent) }
    }

    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]
    fun layoutIndex() = 0
}

```
 
```
DemoApplication需要在AndroidManifest中注册
class DemoApplication : MultiDexApplication() {
    @Inject lateinit var api: Api//dagger2的方式用注解注入

    companion object { const val tomtaw = "/tomtaw/" }

    override fun onCreate() {
        super.onCreate()
        val application = this
        AppLifecycle(application, BR.parse, BR.vm).apply {
            if(BuildConfig.DEBUG)addLocalServer(LocalServer())
            launchDefault {//协程，运行在新线程
                DaggerAppComponent.builder().appModule(AppModule(application)).build().inject(application)
                launchUI {//代码运行在主线程
                    createListener = {
                        ARouter.getInstance().inject(it)//阿里的arouter路由跳转模块，数据注入
                        if (it is AppCompatActivity) applyKitKatTranslucency(it, android.R.color.transparent)
                    }
                    postInitFinish()//子主线程的切换，是异步加载的，这里是传递信号，数据已经加载完成，就会调用BaseActivity中的initData方法
                }
            }
        }
    }
}

//StartActivity中的方法
   
@Route(path = start)
class StartActivity : BindingActivity<LifeViewModel,ActivityStartBinding>() {
    companion object {
        const val start = tomtaw + "start"
    }

    private fun start(){
        ARouterUtil.home()
        TimeUtil.handler.postDelayed({ finish() }, 500)
    }
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        if (!checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
            rxPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribeObserver(onError = { finish() }) {
                    start()
                }
        }else start()
    }


}
//BaseActivity中这段就是判断是否已经加载完了，否则继续等待

abstract class BaseActivity<Model : ViewModel, B> : AppCompatActivity(), Parse<Model, B>,LifecycleInit<Model> {
     open fun ViewGroup.waitFinish(savedInstanceState: Bundle?){
        AppLifecycle.appInit =  {
            removeAllViews()
            addView(inject(savedInstanceState))
        }
    }

    private fun initView(savedInstanceState: Bundle?) {
        val injectView = if (!AppLifecycle.initFinish) {
            startView().apply { waitFinish(savedInstanceState) }
        } else inject(savedInstanceState)
        if (isSwipe() != SwipeBackLayout.FROM_NO) {
            //这里是加载滑动关闭手势，暂时可以忽略
            setContentView(R.layout.activity_base)
            val swipeBackLayout = findViewById<SwipeBackLayout>(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            val imageView: View = findViewById(R.id.iv_shadow)
            swipeBackLayout.onSwipeBackListener =  { _, f -> imageView.alpha = 1 - f }
            swipeBackLayout.addView(injectView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        } else setContentView(injectView)
    }
    
    
    override fun inject(savedInstanceState: Bundle?): View {
        val view = createView(model, this)
        initData(this,savedInstanceState)
        return initToolbar(toolbarView(), view)
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        model.let { if(it is Init)it.initData(this,bundle) }
    }
}
等待完成会在调用inject方法加载view界面

```

现在来看下一个简单的BaseFragment实现.BaseFragment也继承了Parse，所以可以直接使用Parse中解析LayoutView中的layout<br>
```
@Route(path = interrogation)
@LayoutView(layout = [R.layout.fragment_home_intrrogation])
class HomeInterrogationFragment : DataBindingFragment<HomeInterrogationModel, FragmentHomeIntrrogationBinding>() {}
```
后待更新


















