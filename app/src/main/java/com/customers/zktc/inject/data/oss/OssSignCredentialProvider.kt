package com.customers.zktc.inject.data.oss

import android.annotation.SuppressLint
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider
import com.binding.model.ioToMainThread
import com.customers.zktc.inject.data.net.NetApi
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class OssSignCredentialProvider(private val netApi: NetApi) : OSSCustomSignerCredentialProvider() {

    private var result = ""
    private val obj = Object()

    @SuppressLint("CheckResult")
    @Synchronized
    override fun signContent(content: String): String {
        try {
            netApi.ossApi(content)
                .subscribeOn(Schedulers.io())
                .map { it.token }
                .subscribe({this.accept(it)},{it.printStackTrace()})
            synchronized(obj) {
                obj.wait()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Timber.i("oss:%1s", result)
        return result
    }

    private fun accept(string: String) {
        synchronized(obj) {
            result = string
            obj.notify()
        }
    }
}
