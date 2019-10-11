package com.customers.zktc.ui.start

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.ui.start.StartupActivity.Companion.startup

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:24
 * Email: 1033144294@qq.com
 */
@Route(path = startup)
class StartupActivity :BaseActivity<StartupModel>() {
    companion object{ const val startup = ActivityComponent.Config.zktc+"startup"}

}