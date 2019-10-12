package com.customers.zktc.inject.component

import com.customers.zktc.inject.module.ActivityModule
import com.customers.zktc.inject.scope.ActivityScope
import com.customers.zktc.ui.home.HomeActivity
import com.customers.zktc.ui.start.StartupActivity
import com.customers.zktc.ui.user.sign.SignActivity
import com.customers.zktc.ui.user.sign.login.LoginFragment
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent{
    fun inject(activity: HomeActivity)
    fun inject(activity: StartupActivity)
    fun inject(activity: SignActivity)

    object Config{
       const val zktc = "/zktc/"
    }
}