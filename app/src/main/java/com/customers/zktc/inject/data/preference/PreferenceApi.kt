package com.customers.zktc.inject.data.preference

import android.content.Context
import com.customers.zktc.inject.data.preference.setting.SettingApi
import com.customers.zktc.inject.data.preference.user.UserApi

class PreferenceApi (val context: Context){
    private val userApi: UserApi =  UserApi.getInstance(context)
    private val settingApi :SettingApi = SettingApi.getInstance(context)
}