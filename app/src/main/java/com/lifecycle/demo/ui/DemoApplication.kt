package com.lifecycle.demo.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.BR
import com.lifecycle.demo.base.util.applyKitKatTranslucency
import com.lifecycle.demo.inject.component.DaggerAppComponent
import com.lifecycle.demo.inject.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.server.LocalServer
import com.lifecycle.coroutines.util.launchDefault
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.demo.BuildConfig
import javax.inject.Inject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:50
 * Email: 1033144294@qq.com
 */
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
