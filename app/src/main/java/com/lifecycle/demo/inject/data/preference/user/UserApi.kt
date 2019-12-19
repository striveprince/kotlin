package com.lifecycle.demo.inject.data.preference.user

import android.app.Application
import android.content.Context
import com.lifecycle.demo.inject.data.net.bean.TokenBean
import com.lifecycle.demo.inject.data.preference.SharePreferenceUtil


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class UserApi private constructor(context: Context) {
    private val sharePreferenceUtil: SharePreferenceUtil =
        SharePreferenceUtil.getUserInstance(context)
    val tokenEntity: TokenBean
    var login = isLogin
    init {
        tokenEntity = sharePreferenceUtil.getAllDto(TokenBean::class.java)
        isLogin = isLogin(tokenEntity)
    }

    private fun isLogin(tokenEntity: TokenBean): Boolean {
        token = tokenEntity.token?:""
        token_type = tokenEntity.token_type?:""
        login = token.isNotEmpty()&&token_type.isNotEmpty()
        return login
    }


    fun login (it:TokenBean):TokenBean{
        sharePreferenceUtil.setAllDto(it)
        sharePreferenceUtil.getAllDto(tokenEntity)
        isLogin = isLogin(tokenEntity)
        return tokenEntity
    }

    companion object {
        var token: String=""
        var token_type: String=""
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