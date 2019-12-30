package com.lifecycle.binding.impl.adapter

import android.view.View
import com.lifecycle.binding.impl.IListAdapter
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate

class RecyclerAdapter<E:Inflate> : RecyclerOpenAdapter<E, Any>(), IListAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        for (event in events) return event.setEvent(position, e, type, view)
        return setIEntity(e,position, type, view)
    }
}