package com.lifecycle.demo.base.util

import com.lifecycle.binding.util.toast
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.judgeApiThrowable
import com.lifecycle.demo.inject.judgeThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


/**
 *
 * @ProjectName:    kotlin
 * @Package:        com.lifecycle.demo.base.util
 * @ClassName:      FlowUtils
 * @Description:
 * @Author:         A
 * @CreateDate:     2020/4/2 16:19
 * @UpdateUser:     A
 * @UpdateDate:     2020/4/2 16:19
 * @UpdateRemark:
 * @Version:
 */


typealias HttpBlock<T> = suspend () -> InfoEntity<T>

fun <T, R> HttpBlock<T>.restful(function: suspend FlowCollector<R>.(T) -> R): Flow<R> {
    return flow {
        runCatching {
            invoke().run {
                if (code != 0) throw judgeApiThrowable(this)
                if (result != null) emit(function(result))
            }
        }.getOrElse { throw judgeThrowable(it) }
    }
}

fun <T, R> HttpBlock<T>.restfulUI(block: suspend FlowCollector<R>.(T) -> R): Flow<R> {
    return restful(block)
        .flowOn(Dispatchers.IO)
        .catch { toast(it) }
}
