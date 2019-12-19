package com.lifecycle.demo.inject.data.preference

import android.content.Context
import com.lifecycle.demo.inject.data.preference.setting.SettingApi
import com.lifecycle.demo.inject.data.preference.user.UserApi

class PreferenceApi(val context: Context) {
    val userApi: UserApi = UserApi.getInstance(context)
    val settingApi: SettingApi = SettingApi.getInstance(context)
    fun isLogin()=userApi.login
    fun signParams()=userApi.tokenEntity.toParams()

}