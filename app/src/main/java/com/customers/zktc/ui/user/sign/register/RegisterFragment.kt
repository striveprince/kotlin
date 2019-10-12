package com.customers.zktc.ui.user.sign.register

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.inject.component.FragmentComponent.Config.fragment
import com.customers.zktc.ui.user.sign.register.RegisterFragment.Companion.register

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 15:21
 * Email: 1033144294@qq.com
 */
@Route(path = register)
class RegisterFragment :BaseFragment<RegisterModel>() {
    companion object{ const val register = fragment+"register"}
}