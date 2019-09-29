package com.customers.zktc.inject.data.oss

import android.annotation.SuppressLint
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider
import io.reactivex.Observable
import timber.log.Timber


class OssSignCredentialProvider(val observable: Observable<String>) : OSSCustomSignerCredentialProvider() {

    private var result = ""
    private val obj = Object()

    @SuppressLint("CheckResult")
    @Synchronized
    override fun signContent(content: String): String {
        try {
//            observable.subscribe({ this.accept(it) }, { BaseUtil.toast(it) })
            observable.subscribe({this.accept(it)},{it.printStackTrace()})
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
