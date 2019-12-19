package com.lifecycle.demo.inject.component

import android.content.Context
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.AppModule
import com.lifecycle.demo.inject.module.DataModule
import com.lifecycle.demo.inject.qualifier.AppContext
import com.lifecycle.demo.inject.scope.ApplicationScope
import com.lifecycle.demo.ui.DemoApplication
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class])
@ApplicationScope
interface AppComponent {
    fun inject(application: DemoApplication)
    @AppContext fun getContext():Context
    fun getApi(): Api

    fun buildActivityComponent():ActivityComponent.Builder
    fun buildFragmentComponent():FragmentComponent.Builder
}