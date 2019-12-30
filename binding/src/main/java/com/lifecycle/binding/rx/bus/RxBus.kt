package com.lifecycle.binding.rx.bus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/12 12:43
 * Email: 1033144294@qq.com
 */
class RxBus private constructor() {
    companion object {
        private val rxBus = RxBus()
        fun getInstance(): RxBus {
            return rxBus
        }
    }

//    private val bus  = PublishRelay.create<Any>().toSerialized()
    private val bus  = PublishSubject.create<Any>().toSerialized()

    fun send(t: Any){
        bus.onNext(t)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }

}