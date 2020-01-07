package com.lifecycle.demo.inject.data.preference.user

import android.content.Context
import com.lifecycle.binding.util.get
import com.lifecycle.binding.util.put
import com.lifecycle.binding.util.sharedPreferences
import com.lifecycle.demo.inject.data.net.bean.TokenBean

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class UserApi private constructor(context: Context) {
    private val sharedPreferences = context.sharedPreferences("user")
    val tokenEntity: TokenBean =sharedPreferences.get()
    var login = isLogin
    init { isLogin = tokenEntity.isLogin() }

    private fun TokenBean.isLogin(): Boolean {
        UserApi.token = token?:""
        UserApi.token_type = token_type?:""
        return (UserApi.token.isNotEmpty()&&UserApi.token_type.isNotEmpty()).apply { login = this }
    }

    fun login(tokenBean: TokenBean):TokenBean{
        sharedPreferences.put(tokenBean,true)
        return sharedPreferences.get(tokenBean)
    }


    companion object {
        var token: String=""
        var token_type: String=""
        var isLogin = false
        private var userApi: UserApi? = null
        fun getInstance(context: Context) = userApi ?: synchronized(UserApi::class.java) { userApi?: UserApi(context) }
    }
}