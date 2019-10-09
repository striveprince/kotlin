package com.customers.zktc.ui

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.binding.model.App
import com.customers.zktc.BR
import com.customers.zktc.BuildConfig
import com.customers.zktc.inject.component.AppComponent
import com.customers.zktc.inject.component.DaggerAppComponent
import com.customers.zktc.inject.module.AppModule
import com.customers.zktc.inject.module.DataModule

class ZktcApplication : MultiDexApplication() {

    companion object {
        private var application: ZktcApplication? = null
        var component: AppComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        component?.inject(this)
        ARouter.init(this)
        App(this)
        App.debug = BuildConfig.DEBUG
        App.vm = BR.vm
    }
}