package com.binding.model.inflate.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.internal.subscriptions.SubscriptionHelper
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
   private val observer:Observer<T>
) : SingleObserver<T>, Observer<T>, MaybeObserver<T>,
    CompletableObserver, Subscriber<T>,
    LifecycleObserver, AtomicReference<Subscription>(),
    FlowableSubscriber<T>, Subscription, Disposable {
    constructor( onNext: (T) -> Unit = {},
                onError: (Throwable) -> Unit = {},
                onComplete: () -> Unit = {},
                onSubscribe: (Disposable) -> Unit = {}):this(LambdaObserver(onNext,onError,onComplete,onSubscribe))


    private var disposable:Disposable? = null
    override fun onSubscribe(d: Disposable) {
        try {
            disposable = d
            observer.onSubscribe(this)
        } catch (ex: Throwable) {
            Exceptions.throwIfFatal(ex)
            d.dispose()
            onError(ex)

        }
    }

    override fun onSuccess(t: T) {
        onNext(t)
        onComplete()
    }

    override fun onSubscribe(s: Subscription) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                observer.onSubscribe(this)
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
                observer.onNext(t)
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
                observer.onError(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
                onComplete()
            }
        } else {
            RxJavaPlugins.onError(t)
        }
        onComplete()
    }

    override fun onComplete() {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                observer.onComplete()
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


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        dispose()
    }
}