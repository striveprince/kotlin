package com.customers.zktc.ui.user.sign.password.forget

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.inject.component.FragmentComponent.Config.fragment
import com.customers.zktc.ui.user.sign.password.forget.PasswordForgetFragment.Companion.forget

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 14:24
 * Email: 1033144294@qq.com
 */
@Route(path = forget)
class PasswordForgetFragment : BaseFragment<PasswordForgetModel>() {
    companion object{ const val forget = fragment+"forget"}


}