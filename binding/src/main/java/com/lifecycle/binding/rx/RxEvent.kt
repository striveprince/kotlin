package com.lifecycle.binding.rx

import com.lifecycle.binding.IEvent
import io.reactivex.Observable

interface RxEvent<E> : IEvent<E, Observable<Any>> {
}