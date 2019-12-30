package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.demo.inject.module.DataModule
import com.lifecycle.demo.inject.scope.ApplicationScope
import com.lifecycle.demo.ui.DemoApplication
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class])
@ApplicationScope
interface AppComponent {
    fun inject(application: DemoApplication)
    fun getApi(): Api
}