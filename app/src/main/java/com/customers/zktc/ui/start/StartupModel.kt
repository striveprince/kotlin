package com.customers.zktc.ui.start

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.arouter.ARouterUtil
import com.customers.zktc.base.util.checkPermission
import com.customers.zktc.databinding.ActivityStartupBinding
import com.customers.zktc.inject.data.preference.setting.SettingApi
import javax.inject.Inject

@LayoutView(layout = [R.layout.activity_startup])
class StartupModel @Inject constructor() : ViewModel<StartupActivity, ActivityStartupBinding>() {
    @SuppressLint("HardwareIds")
    override fun attachView(savedInstanceState: Bundle?, t: StartupActivity) {
        super.attachView(savedInstanceState, t)
        if (TextUtils.isEmpty(SettingApi.deviceId)) {
                checkPermission(t, Manifest.permission.READ_PHONE_STATE)
                    .subscribeNormal(t,{
                        SettingApi.deviceId = Settings.Secure.getString(t.contentResolver,Settings.Secure.ANDROID_ID)!!
                        //                        val tm = t.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        //                        SettingApi.deviceId = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) tm.imei
                        //                            else tm.deviceId
                        ARouterUtil.start()
                        finish()
                    })
        } else {
            ARouterUtil.start()
            finish()
        }
    }

}