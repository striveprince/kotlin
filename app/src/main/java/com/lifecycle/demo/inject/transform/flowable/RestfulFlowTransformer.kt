package com.lifecycle.demo.inject.transform.flowable

import com.lifecycle.demo.inject.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.reactivestreams.Publisher

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:24
 * Email: 1033144294@qq.com
 */
class RestfulFlowTransformer<T> : FlowableTransformer<InfoEntity<T>, T> {
    override fun apply(upstream: Flowable<InfoEntity<T>>): Publisher<T> {
        return upstream.flatMap {entity->
            Flowable.create<T>({ e ->
                runCatching {
                    if (entity.code == success) entity.result?.let { e.onNext(it) }
                    else  throw judgeApiThrowable(entity)
                }
                    .getOrElse { e.onError(judgeThrowable(it)) }
            }, BackpressureStrategy.BUFFER)
        }
    }
}