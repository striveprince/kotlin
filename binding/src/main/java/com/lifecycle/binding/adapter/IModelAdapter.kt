package com.lifecycle.binding.adapter

import android.view.View
import com.lifecycle.binding.annoation.HandleEvent

interface IModelAdapter<E> : IListAdapter<E> {
    fun clear(){}
    val holderList:List<E>
    fun size(): Int = holderList.size
    fun setIEntity(position: Int, e: E, @HandleEvent type: Int, view: View?): Boolean
}