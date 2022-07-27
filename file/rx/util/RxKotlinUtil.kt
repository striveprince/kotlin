package com.lifecycle.rx.util

import com.lifecycle.binding.util.toast
import com.lifecycle.rx.observer.NormalObserver
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


fun <T> Observable<T>.subscribeNormal(
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
    return observer.disposable.get()
}

fun <T> Observable<T>.subscribeObserver(
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {},
    onNext: (T) -> Unit = {}
){
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
}

//-------------Single---------------

fun <T> Single<T>.subscribeNormal(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) : Disposable {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
    return observer.disposable.get()
}


fun <T> Single<T>.subscribeObserver(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) {
    subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
}


fun <T> Flowable<T>.subscribeObserver(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) {

    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
    onSubscribe.invoke(disposable)
}


fun <T> Flowable<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
): Disposable {
    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
    onSubscribe.invoke(disposable)
    return disposable
}


fun <T> Single<T>.ioToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.ioToMainThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).toMainThread()
}

fun <T> Observable<T>.ioToMainThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).toMainThread()
}

fun<T> Flowable<T>.toMainThread():Flowable<T>{
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun<T> Observable<T>.toMainThread():Observable<T>{
    return this.observeOn(AndroidSchedulers.mainThread())
}



fun <T> Single<T>.toMainThread(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.newToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}


fun <T, R> Observable<List<T>>.concatIterable(block: T.() -> R): Observable<R> =
    this.concatMapIterable {it}
        .map { block(it) }

fun <T, R> Observable<List<T>>.concatList(block: T.() -> R): Observable<List<R>> =
    this.concatIterable(block)
        .toList()
        .toObservable()



