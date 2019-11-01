package com.customers.zktc.inject.data.oss

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.customers.zktc.BuildConfig
import com.customers.zktc.inject.data.net.HttpApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OssApi(context: Context, httpApi: HttpApi) {

    lateinit var client: OSSClient

    init {
//        GlobalScope.launch { client = createClient(context, httpApi) }
        CoroutineScope(Dispatchers.Default).launch{
            client = createClient(context, httpApi)
        }
    }

    private suspend fun createClient(context: Context, httpApi: HttpApi): OSSClient {
        val credentialProvider1 = OssSignCredentialProvider(httpApi)
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000
        conf.socketTimeout = 15 * 1000
        conf.maxConcurrentRequest = 5
        conf.maxErrorRetry = 2
        return OSSClient(context, BuildConfig.endpoint, credentialProvider1, conf)
    }
}