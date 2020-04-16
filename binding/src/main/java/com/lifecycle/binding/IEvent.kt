package com.lifecycle.binding

import android.view.View


interface IEvent<E> {
    fun setEvent( type: Int, e: E,position: Int=-1, view: View?=null):Any
}