package com.lifecycle.rx.adapter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select
import io.reactivex.Observable

class RecyclerSelectAdapter<E : Select>(
    override val max: Int = Int.MAX_VALUE
) : RecyclerOpenAdapter<E>(),
    IList<E>, ISelectList<E> {

    override val selectList: MutableList<E> = ArrayList()
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e, position, type, view))
    }

}