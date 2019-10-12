package com.customers.zktc.ui.user.sign.login

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc
import com.customers.zktc.inject.component.FragmentComponent.Config.fragment
import com.customers.zktc.ui.user.sign.SignActivity.Companion.sign
import com.customers.zktc.ui.user.sign.login.LoginFragment.Companion.login

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/9 14:58
 * Email: 1033144294@qq.com
 */
@Route(path=login)
class LoginFragment :BaseFragment<LoginModel>() {
    companion object{
        const val login = fragment+"login"
    }

}