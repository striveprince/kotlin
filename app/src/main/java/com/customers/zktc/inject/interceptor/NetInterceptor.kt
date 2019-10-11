package com.customers.zktc.inject.interceptor

import com.customers.zktc.inject.data.preference.setting.SettingApi
import com.customers.zktc.inject.data.preference.user.UserApi
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class NetInterceptor @Inject constructor() :Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.newBuilder()
            .addHeader("x-token", UserApi.token)
            .addHeader("x-deviceno", SettingApi.deviceId)
            .addHeader("x-client-type","2")

        return chain.proceed(request)
    }



}