package com.binding.model.adapter

import android.view.View
import com.binding.model.annoation.HandleEve

interface IModelAdapter<E> : IListAdapter<E> {
    fun size(): Int
    fun clear()
    val holderList:List<E>
    fun setIEntity(position: Int, e: E, @HandleEve type: Int, view: View?): Boolean
}