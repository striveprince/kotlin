package com.lifecycle.demo.inject.data.preference.setting

import android.content.Context
import androidx.core.os.bundleOf
import com.lifecycle.binding.util.preference.get
import com.lifecycle.binding.util.preference.putBundle
import com.lifecycle.binding.util.sharedPreferences


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class SettingApi(context: Context) {
    private val sharedPreferences = context.sharedPreferences("setting")
    private val settingEntity = sharedPreferences.get(SettingEntity())
    init { deviceId = settingEntity.deviceId }

    companion object {
        private var settingApi: SettingApi? = null
        fun getInstance(context: Context): SettingApi {
            return settingApi ?: synchronized(SettingApi::class.java) { settingApi?:SettingApi(context) }
        }

        var deviceId: String = ""
            set(value) {
                settingApi?.sharedPreferences?.putBundle(bundleOf("deviceId" to value))
                field = value
            }
    }
}