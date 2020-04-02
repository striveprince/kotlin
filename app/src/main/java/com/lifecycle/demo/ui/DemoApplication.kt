package com.lifecycle.demo.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.BR
import com.lifecycle.demo.base.util.applyKitKatTranslucency
import com.lifecycle.demo.inject.component.DaggerAppComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.server.LocalServer
import com.lifecycle.demo.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:50
 * Email: 1033144294@qq.com
 */
class DemoApplication : MultiDexApplication() {
    @Inject lateinit var api: Api

    companion object { const val tomtaw = "/tomtaw/" }

    override fun onCreate() {
        super.onCreate()
        val application = this
        AppLifecycle(application, BR.parse, BR.vm).apply {
            if(BuildConfig.DEBUG)addLocalServer(LocalServer())
            CoroutineScope(Dispatchers.Default).launch {
                DaggerAppComponent.builder().appModule(AppModule(application)).build().inject(application)
                launch(Dispatchers.Main) {
                    createListener = {
                        ARouter.getInstance().inject(it)
                        if (it is AppCompatActivity) applyKitKatTranslucency(it, android.R.color.transparent)
                    }
                    postInitFinish()
                }
            }
        }

    }



}