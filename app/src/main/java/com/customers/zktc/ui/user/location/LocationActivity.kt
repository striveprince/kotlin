package com.customers.zktc.ui.user.location

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/5 11:56
 * Email: 1033144294@qq.com
 */

@Route(path = LocationActivity.location)
class LocationActivity :BaseActivity<LocationModel>(){
    companion object{ const val location = zktc+"location"}
}