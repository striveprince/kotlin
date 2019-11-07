package com.customers.zktc.ui.user.address

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 9:45
 * Email: 1033144294@qq.com
 */
@Route(path = AddressActivity.address)
class AddressActivity:BaseActivity<AddressModel>() {
    companion object{ const val address = zktc+"address"}
}