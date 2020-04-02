package com.lifecycle.demo.inject.transform.observable

import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.judgeApiThrowable
import com.lifecycle.demo.inject.judgeThrowable
import com.lifecycle.demo.inject.success
import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:24
 * Email: 1033144294@qq.com
 */
class RestfulObserveTransformer<T> : ObservableTransformer<InfoEntity<T>, T> {
    override fun apply(upstream: Observable<InfoEntity<T>>): ObservableSource<T> {
        return upstream.flatMap { entity ->
            Observable.create<T> { e ->
                runCatching {
                    if (entity.code == success) entity.result?.let { e.onNext(it) }
                    else  throw judgeApiThrowable(entity)
                }
                    .getOrElse { e.onError(judgeThrowable(it)) }
            }
        }
    }

}