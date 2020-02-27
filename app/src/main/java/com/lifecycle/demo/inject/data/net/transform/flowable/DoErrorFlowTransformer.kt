package com.lifecycle.demo.inject.data.net.transform.flowable

import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.inject.data.net.exception.AuthenticationException
import com.lifecycle.demo.inject.data.net.exception.TokenExpireException
import com.lifecycle.demo.ui.DemoApplication.Companion.api
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class DoErrorFlowTransformer<T> : FlowableTransformer<T, T> {
    private var retryCount = 0
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.retryWhen { it -> it.ioToMainThread().flatMap { retryToken(it) } }
    }

    private fun retryToken(throwable: Throwable): Flowable<Long> {
        return with(api) {
            when (throwable) {
                is TokenExpireException -> {
//                    httpV1Api.refreshToken(RefreshParams(userApi.userEntity.refresh_token))
//                        .restful().map { userApi.login(it) }
//                        .ioToMainThread()
//                        .map { api.userApi.apply { login.value = userEntity.isLogin() } }
//                        .flatMap { if(++retryCount%3 != 0) Flowable.timer(10,TimeUnit.MILLISECONDS) else Flowable.error(throwable)}
                    Flowable.timer(10,TimeUnit.MILLISECONDS)
                }
                is AuthenticationException -> {
                    ARouterUtil.signIn()
                    Flowable.error(throwable)
                }
                else -> Flowable.error(throwable)
            }
        }
    }
}

