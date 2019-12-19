package com.lifecycle.binding.inter.inflate

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.adapter.IEvent
import com.lifecycle.binding.util.findLayoutView

interface Inflate{
    fun layoutId():Int= findLayoutView(javaClass).layout[0]
    fun createView(context: Context, parent: ViewGroup?=null,convertView:View?=null): View
    fun <E> event(event: IEvent<E>){}
}