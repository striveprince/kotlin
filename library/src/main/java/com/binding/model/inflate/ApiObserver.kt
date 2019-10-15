package com.binding.model.inflate

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/15 16:42
 * Email: 1033144294@qq.com
 */
open class ApiObserver<T> constructor(
    private val onNext: (T) -> Unit = {},
    private val onError:(Throwable)->Unit={},
    private val onComplete:()->Unit = {},
    val onStart:(Any)->Unit = {}
) : SingleObserver<T>, Observer<T>, MaybeObserver<T>, CompletableObserver, Subscriber<T>,
    LifecycleObserver {
    lateinit var disposable:Any

    override fun onSuccess(t: T) {
        onNext(t)
    }

    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }

    override fun onError(e: Throwable) {
        onError.invoke(e)
    }

    override fun onComplete() {
        onComplete.invoke()
    }

    override fun onNext(t: T) {
        onNext.invoke(t)
    }

    override fun onSubscribe(s: Subscription) {
        onStart(s)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        if(disposable is Disposable)
            (disposable as Disposable).dispose()
        else if(disposable is Subscription)
            (disposable as Subscription).cancel()
    }
}