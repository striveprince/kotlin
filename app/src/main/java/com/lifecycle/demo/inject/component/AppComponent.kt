package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.demo.inject.module.DataModule
import com.lifecycle.demo.ui.DemoApplication
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class])
interface AppComponent {
    fun inject(application: DemoApplication)
    fun getApi(): Api
}