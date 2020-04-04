package com.lifecycle.rx.adapter

import android.view.View
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select
import com.lifecycle.rx.IListAdapter
import io.reactivex.Observable

class RecyclerSelectAdapter<E : Select>(
    override val max: Int = Int.MAX_VALUE
) : RecyclerOpenAdapter<E, Observable<Any>>(),
    IListAdapter<E>, ISelectList<E, Observable<Any>> {

    override val selectList: MutableList<E> = ArrayList()
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e, position, type, view))
    }

}