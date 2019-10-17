package com.customers.zktc.inject.data.net.transform

import com.binding.model.toast
import com.customers.zktc.inject.data.net.exception.ApiException
import io.reactivex.*
import io.reactivex.functions.Function

class ErrorThreadTransform<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.onErrorResumeNext(Function {
            if(it is ApiException){
                toast(it)
                Observable.create{ emitter->emitter.onComplete()}
            }else Observable.error(it)
        })
    }
}
