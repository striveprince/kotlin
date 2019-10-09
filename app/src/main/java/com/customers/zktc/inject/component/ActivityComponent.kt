package com.customers.zktc.inject.component

import com.customers.zktc.inject.module.ActivityModule
import com.customers.zktc.inject.scope.ActivityScope
import com.customers.zktc.ui.home.HomeActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent{
    fun inject(activity: HomeActivity)

    object Config{
       const val zktc = "/zktc/"
    }
}