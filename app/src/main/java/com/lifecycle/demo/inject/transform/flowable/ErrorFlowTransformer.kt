package com.lifecycle.demo.inject.transform.flowable

import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.judgeApiThrowable
import com.lifecycle.demo.inject.judgeThrowable
import com.lifecycle.demo.inject.success
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class ErrorFlowTransformer<T> : FlowableTransformer<InfoEntity<T>, InfoEntity<T>> {

    override fun apply(upstream: Flowable<InfoEntity<T>>): Publisher<InfoEntity<T>> {
        return upstream.subscribeOn(Schedulers.io())
            .flatMap {
                Flowable.create<InfoEntity<T>>({ emitter ->
                    if (it.code == success) emitter.onNext(it)
                    else emitter.onError(judgeApiThrowable(it))
                }, BackpressureStrategy.BUFFER)
            }
            .onErrorResumeNext(Function<Throwable, Flowable<InfoEntity<T>>> {
                if (BuildConfig.DEBUG) it.printStackTrace()
                Flowable.error<InfoEntity<T>>(judgeThrowable(it))
            })
    }


}