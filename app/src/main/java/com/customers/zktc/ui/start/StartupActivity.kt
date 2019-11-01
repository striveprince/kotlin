package com.customers.zktc.ui.start

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.binding.model.busPost
import com.binding.model.rxBus
import com.binding.model.subscribeNormal
import com.binding.model.toast
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.inject.component.AppComponent
import com.customers.zktc.ui.ZktcApplication
import com.customers.zktc.ui.start.StartupActivity.Companion.startup
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:24
 * Email: 1033144294@qq.com
 */
@Route(path = startup)
class StartupActivity : BaseActivity<StartupModel>() {
    companion object { const val startup = ActivityComponent.Config.zktc + "startup" }
    override fun initView(savedInstanceState: Bundle?) {
        if (ZktcApplication.component == null) {
            rxBus<AppComponent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNormal({
                initView(savedInstanceState)
            })
        } else super.initView(savedInstanceState)
    }

}