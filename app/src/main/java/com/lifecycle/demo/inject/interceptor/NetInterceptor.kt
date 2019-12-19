package com.lifecycle.demo.inject.interceptor

import com.lifecycle.demo.inject.data.preference.user.UserApi
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class NetInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val r = if (UserApi.isLogin) request.newBuilder()
                .addHeader("Authorization", " ${UserApi.token_type} ${UserApi.token}")
                .addHeader("user-agent", "user-agent")
                .build()
         else request.newBuilder()
                .addHeader("user-agent", "user-agent")
                .build()
        return chain.proceed(r)
    }
}