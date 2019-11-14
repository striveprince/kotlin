package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.module.ActivityModule
import com.lifecycle.demo.inject.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent{
    object Config{ const val zktc = "/zktc/" }
//    fun inject(activity: HomeActivity)
//    fun inject(activity: StartupActivity)
//    fun inject(activity: SignActivity)
//    fun inject(activity: MallCategoryActivity)
//    fun inject(activity: SettingActivity)
//    fun inject(activity: AddressActivity)

}