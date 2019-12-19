package com.lifecycle.demo.inject.data.net.transform

import com.lifecycle.demo.inject.data.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/16 18:01
 * Email: 1033144294@qq.com
 */
class NoErrorObservableTransformer <T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream
            .onErrorResumeNext(Function{
                if(it is ApiException){
                    it.printStackTrace()
                    Observable.create{emitter->emitter.onComplete()}
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