package com.lifecycle.rx.bus

import com.lifecycle.binding.base.bus.Bus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.single.SingleInternalHelper.toObservable
import io.reactivex.subjects.PublishSubject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/12 12:43
 * Email: 1033144294@qq.com
 */
class RxBus<E>  :Bus<E,Disposable> {
    private val bus  = PublishSubject.create<E>().toSerialized()

    override fun send(any: E){
        bus.onNext(any)
    }

    override fun receiver(block: (E) -> Unit):Disposable {
        return bus.subscribe(block)
    }


}
//val rxBus = RxBus<Boolean>()
//    companion object {
//        private val rxBus = RxBus<Any>()
//        fun getInstance(): RxBus<Any> {
//            return rxBus
//        }
//    }
//    private val bus  = PublishRelay.create<Any>().toSerialized()
//    fun <T> toObservableType(eventType: Class<T>): Observable<T> {
//        return bus.ofType(eventType)
//    }
//inline fun <reified E> RxBus<E>.rxBus(): Observable<E> {
//    return toObservableType(E::class.java)
//}
//
//inline fun <reified E> rxBus(): Observable<E> {
//    return RxBus.getInstance()
//        .toObservableType(E::class.java)
//}
//
//
//inline fun<reified E> rxBusMain():Observable<E>{
//    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
//}