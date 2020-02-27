package com.lifecycle.demo.inject.data.net.transform.observable

import com.lifecycle.demo.inject.data.net.InfoEntity
import com.lifecycle.demo.inject.data.judgeApiThrowable
import com.lifecycle.demo.inject.data.judgeThrowable
import com.lifecycle.demo.inject.data.success
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:02
 * Email: 1033144294@qq.com
 */
class ErrorObservableTransformer<T> : ObservableTransformer<InfoEntity<T>, InfoEntity<T>> {

    override fun apply(upstream: Observable<InfoEntity<T>>): ObservableSource<InfoEntity<T>> {
        return upstream.subscribeOn(Schedulers.io())
            .onErrorResumeNext(Function { it ->
                Observable.just(judgeThrowable(it))
                    .flatMap { Observable.error<InfoEntity<T>>(it) }
            })
            .flatMap {
                Observable.create<InfoEntity<T>> { emitter ->
                    if (it.code == success) emitter.onNext(it)
                    else emitter.onError(judgeApiThrowable(it))
                }
            }
    }
}