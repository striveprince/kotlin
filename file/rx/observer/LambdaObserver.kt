package com.lifecycle.rx.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/17 17:39
 * Email: 1033144294@qq.com
 */
class LambdaObserver<T>(
    private val onNext: (T) -> Unit,
    private val onError: (Throwable) -> Unit,
    private val onComplete: () -> Unit,
    private val onSubscribe: (Disposable) -> Unit
) : Observer<T> {
    override fun onComplete() {
        onComplete.invoke()
    }

    override fun onSubscribe(d: Disposable) {
        onSubscribe.invoke(d)
    }

    override fun onNext(t: T) {
        onNext.invoke(t)
    }

    override fun onError(e: Throwable) {
        onError.invoke(e)
    }
}