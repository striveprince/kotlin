package com.customers.zktc.inject.data.net.transform

import com.binding.model.toast
import com.customers.zktc.inject.data.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/16 18:01
 * Email: 1033144294@qq.com
 */
class NoErrorObservableTransformer <T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream
//            .subscribeOn(Schedulers.io())
//            .compose(ErrorObservableTransformer())
            .onErrorResumeNext(Function{
                if(it is ApiException){
                    toast(it)
                    Observable.create<T> { emitter -> emitter.onComplete()}
                }else Observable.error(it)
            })
    }
}

/**
 *
 * return upstream.flatMap {
Single.create<T> { emitter ->  try {
if(it.result!=null){
emitter.onSuccess(it.result)
}else emitter.onError(ApiException(it.code,it.message))
}catch (e:Exception){
emitter.onError(ApiException(it.code,it.message))
} }
}
 * */