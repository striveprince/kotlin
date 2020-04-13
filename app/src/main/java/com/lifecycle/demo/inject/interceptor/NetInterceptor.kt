package com.lifecycle.demo.inject.interceptor

import com.lifecycle.demo.inject.data.preference.user.UserApi
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class NetInterceptor @Inject constructor(private val userApi: UserApi) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val r = request.newBuilder()
                .apply { if (userApi.login.value == true)userApi.userEntity.run { addHeader("Authorization", " $token_type $token") }  }
                .addHeader("user-agent", "user-agent")
                .build()
        return chain.proceed(r)
    }
}