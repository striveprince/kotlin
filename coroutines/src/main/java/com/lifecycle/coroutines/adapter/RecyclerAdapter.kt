package com.lifecycle.coroutines.adapter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate

open class RecyclerAdapter<E : Inflate> : RecyclerOpenAdapter<E>(), IList<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        for (event in events) event.setEvent(position, e, type, view).let { if(it != false)return it }
        return setIEntity(e, position, type, view)
    }
}