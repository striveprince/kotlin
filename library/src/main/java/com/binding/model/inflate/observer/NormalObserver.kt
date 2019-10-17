package com.binding.model.inflate.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.observers.LambdaConsumerIntrospection
import io.reactivex.plugins.RxJavaPlugins
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicReference

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/15 16:42
 * Email: 1033144294@qq.com
 */
open class NormalObserver<T> constructor(
    private val onNext: (T) -> Unit = {},
    private val onError: (Throwable) -> Unit = {},
    private val onComplete: () -> Unit = {},
    private val onSubscribe: (Disposable) -> Unit = {}
) : SingleObserver<T>, Observer<T>, MaybeObserver<T>, CompletableObserver, Subscriber<T>,
    LifecycleObserver, AtomicReference<Subscription>(), FlowableSubscriber<T>, Subscription, Disposable,
    LambdaConsumerIntrospection {
    private var disposable:Disposable? = null
    override fun onSubscribe(d: Disposable) {
        try {
            disposable = d
            onSubscribe.invoke(this)
        } catch (ex: Throwable) {
            Exceptions.throwIfFatal(ex)
            d.dispose()
            onError(ex)
        }
    }

    override fun onSuccess(t: T) {
        onNext(t)
    }

    override fun onSubscribe(s: Subscription) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                onSubscribe.invoke(this)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                s.cancel()
                onError(ex)
            }

        }
    }

    override fun onNext(t: T) {
        if (!isDisposed) {
            try {
                onNext.invoke(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                get().cancel()
                onError(e)
            }
        }
    }

    override fun onError(t: Throwable) {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                onError.invoke(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }

        } else {
            RxJavaPlugins.onError(t)
        }
    }

    override fun onComplete() {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                onComplete.invoke()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }
        }
    }

    override fun dispose() {
        disposable?.dispose()
        cancel()
    }

    override fun isDisposed(): Boolean {
        return when {
            disposable!=null -> disposable!!.isDisposed
            null == get() -> true
            else -> get() === SubscriptionHelper.CANCELLED
        }
    }

    override fun request(n: Long) {
        get().request(n)
    }

    override fun cancel() {
        SubscriptionHelper.cancel(this)
    }

    override fun hasCustomOnError(): Boolean {
        return onError !== Functions.ON_ERROR_MISSING
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        dispose()
    }
}