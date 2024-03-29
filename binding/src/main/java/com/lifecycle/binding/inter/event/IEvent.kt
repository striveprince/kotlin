package com.lifecycle.binding.inter.event

import android.view.View


interface IEvent<E> {
    fun setEvent(type: Int, e: E,position: Int=-1, view: View?=null):Any
    fun setEvent(it: Event<E>) = setEvent(it.type,it.e,it.position,it.view)
}