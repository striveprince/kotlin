package com.lifecycle.rx.bus

import com.lifecycle.binding.base.bus.Bus
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/12 12:43
 * Email: 1033144294@qq.com
 */
class RxBus private constructor() :Bus<Disposable> {
    private val bus  = PublishSubject.create<Any>().toSerialized()
    companion object {
        private val rxBus = RxBus()
        fun getInstance()= rxBus
    }

    override fun send(any: Any){
        bus.onNext(any)
    }

    override fun receiver(block: (Any) -> Unit):Disposable {
        return bus.subscribe(block)
    }

    fun<E> ofType(clazz: Class<E>):Observable<E>{
        return bus.ofType(clazz)
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
//
//
//inline fun<reified E> rxBusMain():Observable<E>{
//    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
//}