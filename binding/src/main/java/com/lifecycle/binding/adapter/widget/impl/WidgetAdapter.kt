package com.lifecycle.binding.adapter.widget.impl

import android.view.View
import com.lifecycle.binding.adapter.inter.IListAdapter
import com.lifecycle.binding.adapter.widget.WidgetOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

abstract class WidgetAdapter<E : Inflate> : WidgetOpenAdapter<E, Observable<Any>>(), IListAdapter<E> {

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e, position, type, view))
    }
}