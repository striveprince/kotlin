package com.customers.zktc.ui.user.sign

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.ui.user.sign.login.LoginFragment

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 11:20
 * Email: 1033144294@qq.com
 */
@Route(path = SignActivity.sign)
class SignActivity :BaseActivity<SignModel>(){
    companion object { const val sign = ActivityComponent.Config.zktc + "sign" }
    @JvmField @Autowired
    var path:String = LoginFragment.login
}