package com.lifecycle.binding.adapter.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.event.IEvent
import com.lifecycle.binding.adapter.event.ObservableEvent
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.Recycler
import java.util.concurrent.atomic.AtomicReference

class RecyclerHolder<E : Inflate,R>(private val v: ViewGroup, inflate: E) : RecyclerView.ViewHolder(inflate.createView(v.context, v)) {
    private val eReference = AtomicReference<E>(inflate)
    fun bindViewHolder(e: E, event: IEvent<E,R>) {
        eReference.set(e)
        eReference.get().let {
            it.event(event)
            it.createView(v.context!!, v, itemView)
        }
    }

    fun onViewDetachedFromWindow() {
        eReference.get().let {
            if (it is Recycler) it.detached(this)
        }
    }

    fun onViewAttachedToWindow() {
        eReference.get().let {
            if (it is Recycler) it.attach(this)
        }
    }


}