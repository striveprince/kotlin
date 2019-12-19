package com.lifecycle.binding.inter.observer

import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.plugins.RxJavaPlugins
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicReference

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/15 16:42
 * Email: 1033144294@qq.com
 */
class NormalObserver<T> constructor(
    private val observer: Observer<T>
) : SingleObserver<T>,
    Observer<T>,
    MaybeObserver<T>,
    Subscriber<T>,
    Disposable,
    CompletableObserver {
    constructor(
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onSubscribe: (Disposable) -> Unit = {}
    ) : this(LambdaObserver(onNext, onError, onComplete, onSubscribe))

    var disposable = AtomicReference<Disposable>()
    private val subscriber: AtomicReference<Subscription> = AtomicReference()

    override fun onSubscribe(d: Disposable) {
        try {
            disposable.compareAndSet(null, d)
            observer.onSubscribe(d)
        } catch (ex: Throwable) {
            Exceptions.throwIfFatal(ex)
            onError(ex)
        }
    }

    override fun onSubscribe(s: Subscription) {
        subscriber.compareAndSet(null, s)
        disposable.compareAndSet(null, this)
    }

    override fun isDisposed(): Boolean {
        return if (subscriber.get() == null) disposable.get().isDisposed
        else subscriber.get() == SubscriptionHelper.CANCELLED
    }

    override fun dispose() {
        subscriber.get()?.cancel()
    }

    override fun onSuccess(t: T) {
        observer.onNext(t)
        observer.onComplete()
    }

    override fun onNext(t: T) {
        try {
            observer.onNext(t)
        } catch (e: Throwable) {
            Exceptions.throwIfFatal(e)
            onError(e)
        }
    }

    override fun onError(t: Throwable) {
        try {
            t.printStackTrace()
            observer.onError(t)
            subscriber.get()?.cancel()
        } catch (e: Throwable) {
            Exceptions.throwIfFatal(e)
            RxJavaPlugins.onError(CompositeException(t, e))
        } finally {
            onComplete()
        }
    }

    override fun onComplete() {
        try {
            observer.onComplete()
        } catch (e: Throwable) {
            Exceptions.throwIfFatal(e)
            RxJavaPlugins.onError(e)
        }
    }
}