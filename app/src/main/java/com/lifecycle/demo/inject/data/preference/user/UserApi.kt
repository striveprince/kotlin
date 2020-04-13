package com.lifecycle.demo.inject.data.preference.user

import android.content.Context
import com.lifecycle.binding.util.*
import com.lifecycle.demo.inject.data.net.bean.TokenBean

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 17:13
 * Email: 1033144294@qq.com
 */
class UserApi(context: Context) {
    private val sharedPreferences = context.application().sharedPreferences("user")
    val userEntity: UserEntity = sharedPreferences.get(UserEntity())

    var login = sharedPreferences.liveData("login", userEntity.isLogin())

    fun login(it: TokenBean) = sharedPreferences.put(it, true).let { sharedPreferences.get(userEntity) }
        .apply { expires_in += (System.currentTimeMillis() / 1000L) }

    fun logout() = sharedPreferences.clear()
        .apply { userEntity.copy(TokenBean()) }
        .apply { login.value = userEntity.isLogin() }

    fun UserEntity.isLogin() = token.isNotEmpty() && token_type.isNotEmpty() && expires_in > System.currentTimeMillis() / 1000L


    companion object {
        private var userApi: UserApi? = null
        fun getInstance(context: Context) = userApi ?: synchronized(UserApi::class.java) { userApi ?: UserApi(context).also { userApi = it } }
    }
}