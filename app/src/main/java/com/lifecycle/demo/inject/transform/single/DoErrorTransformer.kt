package com.lifecycle.demo.inject.transform.single

import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.base.util.api
import com.lifecycle.demo.inject.AuthenticationException
import com.lifecycle.demo.inject.TokenExpireException
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

class DoErrorTransformer<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.retryWhen { it -> it.ioToMainThread().flatMap { retryToken(it) } }
    }

    private fun retryToken(throwable: Throwable): Flowable<T> {
        return with(api) {
            when (throwable) {
                is TokenExpireException -> {
//                    httpV1Api.refreshToken(RefreshParams(api.userApi.userEntity.refresh_token))
//                        .restful().map { userApi.login(it) }
//                        .ioToMainThread()
//                        .map { api.userApi.apply { login.value = userEntity.isLogin() } }
//                        .flatMap { Flowable.empty<T>() }
                    Flowable.empty<T>()
                }
                is AuthenticationException -> {
                    ARouterUtil.signIn()
                    Flowable.error(throwable)
                }
                else -> Flowable.error<T>(throwable)
            }
        }
    }
}

