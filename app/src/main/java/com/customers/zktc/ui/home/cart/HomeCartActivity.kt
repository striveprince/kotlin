package com.customers.zktc.ui.home.cart

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.ui.home.cart.HomeCartActivity.Companion.cart

@Route(path = cart)
class HomeCartActivity:BaseActivity<HomeCartModel>() {
    companion object { const val cart = ActivityComponent.Config.zktc + "home" }

}