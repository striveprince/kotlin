package com.customers.zktc.ui

import androidx.multidex.MultiDexApplication
import com.binding.model.App
import com.customers.zktc.BR
import com.customers.zktc.inject.component.AppComponent
import com.customers.zktc.inject.component.DaggerAppComponent
import com.customers.zktc.inject.module.AppModule

class ZktcApplication : MultiDexApplication(){
    val appComponent: AppComponent by lazy(DaggerAppComponent.builder().appModule(AppModule(application!!))::build)
    companion object {
        var application:ZktcApplication? = null
        var component:AppComponent? = null
    }
    override fun onCreate() {
        super.onCreate()
        application = this
        App(this)
        App.debug = false
        App.vm = BR.vm
        component = appComponent
    }
}