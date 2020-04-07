# kotlin
parse interface use create view could create view
it support databinding,anko and normal way to create view
you could read the demo and build project what you want 

implementation 'com.lifecycle:binding:2.1.0'


从两三年前，就一直想介绍一下自己在实战的项目结构
地址：[https://github.com/striveprince/kotlin](https://github.com/striveprince/kotlin)

还记得我最初写的时候。android官方的lifecycle，viewmodel和liveData都还没有发布，
从去年开始，我将之前的项目架构重新用kotlin重写了，同时引入databinding,dagger,retrofit,rxjava,协程等，最后将rx和协程拆开，好了，先看下面项目结构吧

首先现在有Parse和Inflate两个接口,都是可以解析@LayoutView的layout
A-->B这个符号表示B是A的子类
```
Parse-->Normal
         -->AnkoParse 
         -->Binding-->DataBinding-->DataBindInflate-->DataBeanInflate
                                                                            -->BindingActivity-->DatabindingActivity
         -->BaseActivity-->NormalActivity
                                 -->RecyclerActivity
                                 -->AnkoActivity
                                 -->BindingActivity-->DatabindingActivity
        -->BaseFragment(同上)
```
Parse是用于什么场景，可以看到这里有BaseActivity和BaseFragment,这里其实就是将解析视图和数据同时引入，就是有数据和界面两个类同时进行绑定
Normal：LayoutInflater.from(context).inflate的方式解析
AnkoParse：anko的方式解析
Binding：viewBinding解析泛型类型加载
DataBinding：databinding解析
DataBindInflate：用于复用型View的解析
BaseActivity：就是未定义解析方式的基本Activity,子类就是以上面几种方式解析的方式使用的方式
BaseFragment：就是未定义解析方式的基本Fragment,同BaseActivity
```
Inflate-->Diff                -->ErrorInflate
       -->BindingInflate      -->ViewBindInflate
                              -->ListViewInflate
       -->DataBindInflate     -->DataBeanInflate
       -->Select
```
Inflate：就是单纯的解析界面用于复用界面
Diff:计算是否是同一个数据
ErrorInflate：错误Inflate，可自定义
BindingInflate：Databinding解析
ViewBindInflate:BindingInflate的一个简单的实现类
DataBindInflate：用于解析和界面分开，可以用于复用界面
Select:将用于选择，配合RecyclerSelectAdapter
DataBeanInflate:加入Attach和Detach，就是实现了attach和detach方法，
另外还有Attach,Detach用于界面移入和移出时被调用（目前支持recyclerView）
```
IEvent-->IList-->IListAdapter
                      -->ListModel
                      -->ListAdapter
                      -->ListViewInflate
                      -->ISelect
                      -->FragmentOpenAdapter
                      -->WidgetOpenAdapter
                      -->FragmentOpenPager2Adapter
                      -->WidgetOpenAdapter
                      -->RecyclerOpenAdapter
```
IEvent：事件类，可以使用这个传入事件进行监听
IList：复用界面事件类，是各种adapter的基类,可以实现列表的加载，刷新，删除，更新等操作
ListModel：用于和adapter的数据类，可以使用adapter和数据结合的一个类，可以仔细看其子类ListViewModel
ISelect：用于选择场景
ListInflate：Inflate和IList结合的一个接口
ListViewInflate：ListInflate的一个实现类
由于以上很多都是的接口，所以很多都可以自由组合。比如ListInflate,DataBindInflate等

实现
  是Application中onCreate的中先注册AppLifecycle.这里实现了异步注册，在注册完成的时候，postInitFinish方法会发送注册完成通知其他界面，BaseActivity中的initData方法会等待注册postIntiFinish方法调用，具体逻辑可以看代码。
```
Application中的
  @Inject lateinit var api: Api
    override fun onCreate() {
        super.onCreate()
        val application = this
        AppLifecycle(application, BR.parse, BR.vm).apply {
            if(BuildConfig.DEBUG)addLocalServer(LocalServer())
            launchDefault {
                DaggerAppComponent.builder().appModule(AppModule(application)).build().inject(application)
                launchUI {
                    createListener = {
                        ARouter.getInstance().inject(it)
                        if (it is AppCompatActivity) applyKitKatTranslucency(it, android.R.color.transparent)
                    }
                    postInitFinish()
                }
            }
        }
    }

//StartActivity中的方法
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        ARouterUtil.start()
    }
//BaseActivity中这段就是判断是否已经加载完了，否则继续等待
      val injectView = if (!AppLifecycle.initFinish) {
      startView().apply { waitFinish(savedInstanceState) }

 open fun ViewGroup.waitFinish(savedInstanceState: Bundle?){
        AppLifecycle.appInit =  {
            removeAllViews()
            addView(inject(savedInstanceState))
        }
    }
等待完成会在调用inject方法加载view界面

```

现在来看下一个简单的BaseFragment实现.
BaseFragment也继承了Parse，所以可以直接使用Parse中解析LayoutView中的layout
```
@Route(path = interrogation)
@LayoutView(layout = [R.layout.fragment_home_intrrogation])
class HomeInterrogationFragment : DataBindingFragment<HomeInterrogationModel, FragmentHomeIntrrogationBinding>() {}
```
后待更新


















