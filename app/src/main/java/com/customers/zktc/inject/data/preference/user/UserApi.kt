package com.customers.zktc.inject.data.preference.user

import android.app.Application
import android.content.Context
import com.customers.zktc.inject.data.preference.SharePreferenceUtil


/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class UserApi private constructor(context: Context) {
    private val sharePreferenceUtil: SharePreferenceUtil = SharePreferenceUtil.getUserInstance(context)
    private val userEntity : UserEntity
        init {
        userEntity = sharePreferenceUtil.getAllDto(UserEntity::class.java)
        isLogin(userEntity)
    }

    private fun isLogin(userEntity: UserEntity):Boolean{
        return true
    }

    companion object {
        var token = ""
        private var userApi: UserApi? = null
        fun getInstance(context: Context): UserApi {
            var util: UserApi? =
                userApi
            if (util == null) {
                synchronized(UserApi::class.java) {
                    util = userApi
                    if (util == null) {
                        var context1 = context
                        if (context !is Application)
                            context1 = context.applicationContext
                        util = UserApi(context1)
                        userApi = util
                    }
                }
            }
            return util!!
        }


    }






}