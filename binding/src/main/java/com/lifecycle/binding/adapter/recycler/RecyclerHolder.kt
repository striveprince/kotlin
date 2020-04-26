package com.lifecycle.binding.adapter.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.Recycler
import java.util.concurrent.atomic.AtomicReference

@Suppress("UNCHECKED_CAST")
class RecyclerHolder<E : Inflate>(private val v: ViewGroup, inflate: E) : RecyclerView.ViewHolder(inflate.createView(v.context, v)) {
    private val eReference = AtomicReference<E>(inflate)
    fun bindViewHolder(e: E, event: IEvent<*>) {
        eReference.set(e)
        eReference.get().let {
            it.event(event as IEvent<Any>)
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