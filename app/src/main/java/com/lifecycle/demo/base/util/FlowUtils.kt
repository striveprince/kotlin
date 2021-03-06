package com.lifecycle.demo.base.util

import com.lifecycle.binding.util.toast
import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.judgeApiThrowable
import com.lifecycle.demo.inject.success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


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
typealias ConvertFlow<T, R> = suspend FlowCollector<R>.(T) -> R

fun <T, R> HttpBlock<T>.restful(function: ConvertFlow<T, R>): Flow<R> {
    return runBlocking(Dispatchers.IO) {
        runCatching { invoke() }.getOrElse {
            InfoEntity(2, it.message() ?: "", null)
                .apply { it.printStackTrace() }
        }
    }
        .run {
            flow<R> {
                if (code != success) throw judgeApiThrowable(this@run)
                if (result != null) emit(function(result))
            }
        }
}

private  fun Throwable.message(): String? {
    return message?:cause?.message()
}

fun <T, R> HttpBlock<T>.restfulUI(block: suspend FlowCollector<R>.(T) -> R): Flow<R> {
    return restful(block)
        .flowOn(Dispatchers.IO)
        .catch { toast(it) }
}
