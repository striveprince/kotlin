package com.lifecycle.demo.inject.transform.single

import com.lifecycle.demo.inject.*
import io.reactivex.*

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:24
 * Email: 1033144294@qq.com
 */
class RestfulSingleTransformer<T> : SingleTransformer<InfoEntity<T>, T> {

    override fun apply(upstream: Single<InfoEntity<T>>): SingleSource<T> {
        return upstream.flatMap { entity ->
            Single.create<T> { e ->
                runCatching {
                    if (entity.code == success) entity.result?.let { e.onSuccess(it) }
                    else throw judgeApiThrowable(entity)
                }
                    .getOrElse { e.onError(judgeThrowable(it)) }
            }
        }
    }
}