package com.customers.zktc.inject.data.preference.user

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.customers.zktc.inject.data.preference.SharePreferenceUtil
import com.customers.zktc.ui.user.sign.SignEntity


/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class UserApi private constructor(context: Context) {
    private val sharePreferenceUtil: SharePreferenceUtil =
        SharePreferenceUtil.getUserInstance(context)
    private val userEntity: UserEntity
    var login = isLogin
    init {
        userEntity = sharePreferenceUtil.getAllDto(UserEntity::class.java)
        isLogin = isLogin(userEntity.token)

    }

    private fun isLogin(token:String): Boolean {
        login = !TextUtils.isEmpty(token)
        return login
    }

    fun login(it: SignEntity): UserEntity {
        sharePreferenceUtil.setAllDto(it)
        sharePreferenceUtil.setAllDto(it.customerVo)
        sharePreferenceUtil.setAllDto(it.customerVo.extWechatCustomerVo)
        sharePreferenceUtil.getAllDto(userEntity)
        isLogin = isLogin(userEntity.token)
        return userEntity
    }

    companion object {
        var token = ""
        var isLogin = false
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