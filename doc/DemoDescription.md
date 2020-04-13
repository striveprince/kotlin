首先来看下demo项目的目录结构<br>
主模块分为3个目录<br>
&ensp;&ensp;1.base:主要用于通用的全局工具支持，如arouter、util和datbinding目录等 anko目录暂时可以忽略<br>
&ensp;&ensp;2.inject:主要用于数据事件的目录，这里使用的是dagger2的注解注入的方式再application启动的时候就加载，具体的dagger2的加载可以查看这个链接[https://blog.csdn.net/shusheng0007/article/details/80950117]，这里用的是最简单的方式<br>
&ensp;&ensp; &ensp;&ensp;interceptor:过滤器，主要用于okhttp的过滤器<br>
&ensp;&ensp; &ensp;&ensp;module:dagger的module，提供数据<br>
&ensp;&ensp; &ensp;&ensp;ApiException:所有异常及异常处理<br>
&ensp;&ensp; &ensp;&ensp;InfoEntity:数据的包装类<br>
&ensp;&ensp; &ensp;&ensp;Api:这个类就将所有的数据其中再这里了，如oss的上传下载，http数据的访问，preference本地数据的存储与读取等<br>
   3.ui:主要的业务实现代码
DemoApplication:主applicaiton需要再AndroidManifest.xml中注册
```
class DemoApplication : MultiDexApplication() {
    @Inject
    lateinit var api: Api//dagger2的方式用注解注入

    companion object {
        const val tomtaw = "/tomtaw/"
    }

    override fun onCreate() {
        super.onCreate()
        val application = this
        AppLifecycle(application, BR.parse, BR.vm).apply {
            if (BuildConfig.DEBUG) addLocalServer(LocalServer())
            launchDefault {//协程，运行在新线程
                DaggerAppComponent.builder().appModule(AppModule(application)).build().inject(application)//dagger数据注入
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

```
start/StartActivity:启动的Activity
```
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
```
协程的方式：继承RecyclerDiffFragment后启动是会调用require方法，这个时候可以直接使用flow的
rxjava的方式：继承RecyclerDiffFragment后启动是会调用apiData方法，这个时候可以直接使用rxjava的Single方式来获取数据
他们都会解析InterrogationListEntity上的注解@LayoutView，获取对应的id和binding，然后将数据自动绑定进去
```
@Route(path = interrogationList)
class InterrogationListFragment : RecyclerDiffFragment<InterrogationListEntity>() {

    companion object {
        const val interrogationList = "$interrogation/list"
    }
    

    override suspend fun require(startOffset: Int, state: Int): Flow<List<InterrogationListEntity>> {
        val taskCategory = when (arguments?.getString(Constant.params)) {
            "new" -> 1
            "wait" -> 2
            else -> 0
        }
        return api.getInterrogationList(taskCategory, startOffset, state)
    }

    private suspend fun Api.getInterrogationList(taskCategory: Int, position: Int, state: Int): Flow<List<InterrogationListEntity>> {
        val params = InterrogationParams(taskCategory, position)
        return suspend{ netApi.httpApi.getInterrogationList(params) }
            .restful {   it.result.toEntities() }
    }

    
}

这个InterrogationListEntity和InterrogationBean两个对象会分别自动绑定在holder_interrogation.xml的parse和vm属性上

@LayoutView(layout = [R.layout.holder_interrogation])
class InterrogationListEntity(private val bean: InterrogationBean) : DataBindInflate<InterrogationBean, ViewDataBinding>, Diff,Recycler,TimeEntity {
    fun onItemClick(v: View) {
        ARouterUtil.interrogationDetail(bean.id)
    }
}

holder_interrogation.xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="vm"
            type="com.lifecycle.demo.inject.data.net.bean.InterrogationBean" />
        <variable
            name="parse"
            type="com.lifecycle.demo.ui.home.interrogation.rxlist.InterrogationListEntity" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:background="@color/item_press"
        android:onClick="@{parse::onItemClick}"
        android:paddingLeft="12dp"
        android:paddingTop="16dp"
        android:paddingRight="12dp"
        android:paddingBottom="18dp">
    </androidx.constraintlayout.widget.ConstraintLayout>
```
后面再细讲preference的代理绑定及databinding的双向绑定，已经rxjava的错误等，待续
