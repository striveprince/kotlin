package com.lifecycle.rx.bus

import com.lifecycle.binding.base.bus.Bus
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

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


