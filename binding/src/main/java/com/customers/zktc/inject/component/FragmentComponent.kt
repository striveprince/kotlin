package com.customers.zktc.inject.component

import com.customers.zktc.inject.component.ActivityComponent.Config.zktc
import com.customers.zktc.inject.module.FragmentModule
import com.customers.zktc.inject.scope.FragmentScope

import dagger.Component

@Component(dependencies = [AppComponent::class],modules = [FragmentModule::class])
@FragmentScope
interface FragmentComponent {


    object Config{
        const val fragment = zktc+"fragment/"
    }
}