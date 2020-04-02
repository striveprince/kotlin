package com.lifecycle.demo.base.util

import com.lifecycle.binding.util.toast
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
fun <T, R> T.restful(block: suspend FlowCollector<R>.(T) -> R): Flow<R> {
    return flow {
        emit(
            runCatching { block(this@restful) }
                .getOrElse { throw judgeThrowable(it) }
        )
    }
}

fun <T, R> T.restfulUI(block: suspend FlowCollector<R>.(T) -> R): Flow<R> {
    return restful(block)
        .flowOn(Dispatchers.IO)
        .catch { toast(it) }
}
