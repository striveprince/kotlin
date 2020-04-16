package com.lifecycle.rx.adapter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

open class RecyclerAdapter<E:Inflate> : RecyclerOpenAdapter<E>(), IList<E> {
    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        for (event in events) event.setEvent(type, e, position, view).let { if(it != false)return it }
        return Observable.just(setIEntity(e,position, type, view))
    }
}