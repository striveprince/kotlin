package com.customers.zktc.ui.user.login

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc
import com.customers.zktc.ui.user.login.LoginActivity.Companion.login

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/9 14:58
 * Email: 1033144294@qq.com
 */
@Route(path = login)
class LoginActivity :BaseActivity<LoginModel>() {

    companion object { const val login = zktc + "login" }

}