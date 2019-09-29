package com.customers.zktc.inject.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class NetInterceptor :Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }
}