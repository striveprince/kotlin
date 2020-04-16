package com.lifecycle.coroutines.adapter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.widget.WidgetOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate

open class WidgetAdapter<E : Inflate> : WidgetOpenAdapter<E>(), IList<E> {

    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        for (event in events) return event.setEvent(type, e, position, view)
        return setIEntity(e, position, type, view)
    }
}