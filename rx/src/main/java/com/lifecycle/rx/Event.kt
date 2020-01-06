package com.lifecycle.rx

import com.lifecycle.binding.IEvent
import io.reactivex.Observable

interface Event<E> : IEvent<E, Observable<Any>> {
}