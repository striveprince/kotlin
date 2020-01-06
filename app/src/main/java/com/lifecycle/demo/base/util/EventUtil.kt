package com.lifecycle.demo.base.util

//inline fun <reified E> rxBus(): Observable<E> {
//    return RxBus.getInstance()
//        .toObservableType(E::class.java)
//}
//
//inline fun<reified E> rxBusMain():Observable<E>{
//    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
//}
//
//fun busPost(any: Any) {
//    return RxBus.getInstance().send(any)
//}