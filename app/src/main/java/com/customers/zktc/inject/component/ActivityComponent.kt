package com.customers.zktc.inject.component

import com.customers.zktc.inject.module.ActivityModule
import com.customers.zktc.inject.scope.ActivityScope
import com.customers.zktc.ui.home.HomeActivity
import com.customers.zktc.ui.mall.catagory.MallCategoryActivity
import com.customers.zktc.ui.start.StartupActivity
import com.customers.zktc.ui.user.address.AddressActivity
import com.customers.zktc.ui.user.setting.SettingActivity
import com.customers.zktc.ui.user.sign.SignActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent{
    object Config{ const val zktc = "/zktc/" }
    fun inject(activity: HomeActivity)
    fun inject(activity: StartupActivity)
    fun inject(activity: SignActivity)
    fun inject(activity: MallCategoryActivity)
    fun inject(activity: SettingActivity)
    fun inject(activity: AddressActivity)

}