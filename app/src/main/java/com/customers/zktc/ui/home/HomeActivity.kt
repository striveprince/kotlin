package com.customers.zktc.ui.home

import com.alibaba.android.arouter.facade.annotation.Route
import com.binding.model.base.view.SwipeBackLayout.Companion.FROM_NO
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc
import com.customers.zktc.ui.home.HomeActivity.Companion.home

@Route(path = home)
class HomeActivity : BaseActivity<HomeModel>() {
    companion object { const val home = zktc + "home" }
    override fun isSwipe(): Int {
        return FROM_NO
    }
}