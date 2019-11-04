package com.customers.zktc.ui.user.setting

import android.view.View
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.databinding.ActivitySettingBinding
import com.customers.zktc.inject.data.Api
import javax.inject.Inject

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/4 13:03
 * Email: 1033144294@qq.com
 */

@LayoutView(layout = [R.layout.activity_setting])
class SettingModel @Inject constructor(): ViewModel<SettingActivity,ActivitySettingBinding>(){

    @Inject lateinit var api:Api
    fun onLogoutClick(v: View){
        api.logout().subscribeNormal (v,t.lifecycle,{ finish() })
    }
}