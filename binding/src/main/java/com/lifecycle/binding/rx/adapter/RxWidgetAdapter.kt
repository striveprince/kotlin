package com.lifecycle.binding.rx.adapter

import android.view.View
import com.lifecycle.binding.rx.IRxListAdapter
import com.lifecycle.binding.adapter.widget.WidgetOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

open class RxWidgetAdapter<E : Inflate> : WidgetOpenAdapter<E, Observable<Any>>(), IRxListAdapter<E> {

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e, position, type, view))
    }
}