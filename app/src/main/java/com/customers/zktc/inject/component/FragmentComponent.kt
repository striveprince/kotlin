package com.customers.zktc.inject.component

import com.customers.zktc.inject.module.FragmentModule
import com.customers.zktc.inject.scope.FragmentScope
import com.customers.zktc.ui.home.mine.HomeMineFragment
import com.customers.zktc.ui.home.cart.HomeCartFragment
import com.customers.zktc.ui.home.classify.HomeClassifyFragment
import com.customers.zktc.ui.home.member.HomeMemberFragment
import com.customers.zktc.ui.home.page.HomePageFragment
import dagger.Component

@Component(dependencies = [AppComponent::class],modules = [FragmentModule::class])
@FragmentScope
interface FragmentComponent {
    fun inject(activity: HomePageFragment)
    fun inject(activity: HomeCartFragment)
    fun inject(activity: HomeClassifyFragment)
    fun inject(activity: HomeMineFragment)
    fun inject(activity: HomeMemberFragment)

}