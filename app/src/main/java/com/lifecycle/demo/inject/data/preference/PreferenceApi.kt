package com.lifecycle.demo.inject.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.lifecycle.binding.util.beanSetValue
import com.lifecycle.binding.util.copy
import com.lifecycle.binding.util.getAllFields
import com.lifecycle.binding.util.noDelegateName
import com.lifecycle.demo.inject.data.net.bean.TokenBean
import com.lifecycle.demo.inject.data.preference.setting.SettingApi
import com.lifecycle.demo.inject.data.preference.user.UserApi

class PreferenceApi(val context: Context) {
    val userApi: UserApi = UserApi.getInstance(context)
    val settingApi: SettingApi = SettingApi.getInstance(context)
    fun isLogin()=userApi.login.value == true
    fun signParams()= userApi.userEntity.toParams()
}