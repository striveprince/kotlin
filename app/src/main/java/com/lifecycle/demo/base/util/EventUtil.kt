package com.lifecycle.demo.base.util

import com.lifecycle.rx.bus.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

inline fun <reified E> rxBus(): Observable<E> {
    return RxBus.getInstance()
        .toObservableType(E::class.java)
}

inline fun<reified E> rxBusMain():Observable<E>{
    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
}

fun busPost(any: Any) {
    return RxBus.getInstance().send(any)
}