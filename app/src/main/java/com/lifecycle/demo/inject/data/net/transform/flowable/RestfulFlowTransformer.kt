package com.lifecycle.demo.inject.data.net.transform.flowable

import com.lifecycle.demo.inject.data.net.InfoEntity
import com.lifecycle.demo.inject.data.net.exception.ApiException
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
            Flowable.create<T>({e->
                runCatching { entity.result?.let { e.onNext(it) } }
                    .getOrElse { e.onError(ApiException(entity.code, entity.msg, entity)) }
            },BackpressureStrategy.BUFFER)
        }
    }
}