package com.lifecycle.binding.adapter.event

import io.reactivex.Observable

interface ObservableEvent<E> : IEvent<E, Observable<Any>> {
}