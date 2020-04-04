package com.lifecycle.binding

import android.view.View


interface IEvent<E> {
    fun setEvent(position: Int, e: E, type: Int, view: View?):Any
}