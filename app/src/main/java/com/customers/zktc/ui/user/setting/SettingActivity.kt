package com.customers.zktc.ui.user.setting

import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent.Config.zktc

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/4 13:03
 * Email: 1033144294@qq.com
 */
@Route(path = "")
class SettingActivity:BaseActivity<SettingModel> (){
    companion object  { const val setting = zktc+"setting" }


}