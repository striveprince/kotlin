package com.lifecycle.demo.inject.transform.single

import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.judgeApiThrowable
import com.lifecycle.demo.inject.judgeThrowable
import com.lifecycle.demo.inject.success
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:02
 * Email: 1033144294@qq.com
 */
class ErrorSingleTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                if (BuildConfig.DEBUG) it.printStackTrace()
                Single.error(judgeThrowable(it))
            }
            .flatMap {
                Single.create<T> { emitter ->
                    if (it is InfoEntity<*>) {
                        if (it.code == success) emitter.onSuccess(it)
                        else emitter.onError(judgeApiThrowable(it))
                    }else emitter.onSuccess(it)
                }
            }
    }
}
