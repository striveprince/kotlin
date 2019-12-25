package com.lifecycle.binding.adapter.recycler.impl

import android.view.View
import com.lifecycle.binding.adapter.inter.IListAdapter
import com.lifecycle.binding.adapter.inter.IListEventAdapter
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

class RecyclerEventAdapter<E:Inflate> : RecyclerOpenAdapter<E, Any>(), IListEventAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        for (event in events) return event.setEvent(position, e, type, view)
        return setIEntity(e,position, type, view)
    }
}