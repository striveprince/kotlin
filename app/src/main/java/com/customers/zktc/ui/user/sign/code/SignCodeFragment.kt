package com.customers.zktc.ui.user.sign.code

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.inject.component.FragmentComponent.Config.fragment
import com.customers.zktc.ui.user.sign.code.SignCodeFragment.Companion.signCode

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 15:15
 * Email: 1033144294@qq.com
 */
@Route(path = signCode)
class SignCodeFragment:BaseFragment<SignCodeModel>() {
    companion object{
        const val signCode = fragment+"code"
    }
}