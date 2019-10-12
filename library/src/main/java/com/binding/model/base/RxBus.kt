package com.binding.model.base

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

/**
 * Company: 中科同创
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

    private val bus  = PublishRelay.create<Any>().toSerialized()

    fun send(t: Any){
        bus.accept(t)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }

}