package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.component.ActivityComponent.Config.zktc
import com.lifecycle.demo.inject.module.FragmentModule
import com.lifecycle.demo.inject.scope.FragmentScope
import dagger.Component

@Component(dependencies = [AppComponent::class],modules = [FragmentModule::class])
@FragmentScope
interface FragmentComponent {
//    fun inject(activity: HomePageFragment)
//    fun inject(activity: HomeCartFragment)
//    fun inject(activity: HomeClassifyFragment)
//    fun inject(activity: HomeMineFragment)
//    fun inject(activity: HomeMemberFragment)
//    fun inject(fragment: LoginFragment)
//    fun inject(fragment: SignCodeFragment)
//    fun inject(fragment: PasswordForgetFragment)
//    fun inject(fragment: RegisterFragment)

    object Config{
        const val fragment = zktc+"fragment/"
    }
}