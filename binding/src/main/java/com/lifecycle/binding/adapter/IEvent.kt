package com.lifecycle.binding.adapter

import android.view.View
import io.reactivex.Observable

interface IEvent<E> {
    fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any>
}