package com.lifecycle.demo.inject.data.preference.setting

import android.app.Application
import android.content.Context
import com.lifecycle.demo.inject.data.preference.SharePreferenceUtil


/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class SettingApi private constructor(context: Context) {
    private val sharePreferenceUtil = SharePreferenceUtil.getSettingInstance(context)
    private val settingEntity : SettingEntity
    init {
        settingEntity = sharePreferenceUtil.getAllDto(SettingEntity::class.java)
        deviceId = settingEntity.deviceId
    }

    companion object {
        var deviceId: String = ""
        set(value) { settingApi?.sharePreferenceUtil?.setValue("deviceId",value) }
        private var settingApi: SettingApi? = null
        fun getInstance(context: Context): SettingApi {
            var util: SettingApi? = settingApi
            if (util == null) {
                synchronized(SettingApi::class.java) {
                    util = settingApi
                    if (util == null) {
                        var context1 = context
                        if (context !is Application)
                            context1 = context.applicationContext
                        util = SettingApi(context1)
                        settingApi = util
                    }
                }
            }

            return util!!
        }
    }






}