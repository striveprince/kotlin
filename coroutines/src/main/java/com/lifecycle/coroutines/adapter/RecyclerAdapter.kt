package com.lifecycle.coroutines.adapter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate

open class RecyclerAdapter<E : Inflate> : RecyclerOpenAdapter<E>(), IList<E> {
    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        for (event in events) event.setEvent(type, e, position, view).let { if(it != false)return it }
        return setIEntity(e, position, type, view)
    }
}