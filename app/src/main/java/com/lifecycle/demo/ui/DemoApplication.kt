package com.lifecycle.demo.ui

import androidx.multidex.MultiDexApplication
import com.lifecycle.binding.App
import com.lifecycle.demo.base.util.busPost
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:50
 * Email: 1033144294@qq.com
 */
class DemoApplication : MultiDexApplication() {
    @Inject
    lateinit var api: Api

    companion object {
        lateinit var application: DemoApplication
        lateinit var api: Api
    }

    override fun onCreate() {
        super.onCreate()
        val application = this
//        CoroutineScope(Dispatchers.Default).launch {
//            DaggerAppComponent.builder()
//                .appModule(AppModule(application))
//                .build()
//                .inject(application)
//            launch(Dispatchers.Main) {
//                App(application)
//                DemoApplication.api = api
//                busPost(true)//notify base activity application and resource init completed
//            }
//        }
        Timber.i("init TomtawApplication")
    }
}