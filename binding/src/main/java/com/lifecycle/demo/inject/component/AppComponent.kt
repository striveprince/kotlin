package com.lifecycle.demo.inject.component

import android.content.Context
import com.lifecycle.demo.ui.DemoApplication
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.demo.inject.module.DataModule
import com.lifecycle.demo.inject.qualifier.context.AppContext
import com.lifecycle.demo.inject.scope.ApplicationScope
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class])
@ApplicationScope
interface AppComponent {
    fun inject(application: DemoApplication)
    @AppContext fun getContext():Context
    fun getApi(): Api
}