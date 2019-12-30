package com.lifecycle.binding

import android.view.View


interface IEvent<E,R> {
    fun setEvent(position: Int, e: E, type: Int, view: View?): R
}