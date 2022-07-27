package com.lifecycle.binding.adapter.recycler

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate

@Suppress("UNCHECKED_CAST")
open class RecyclerAdapter<E : Inflate> : RecyclerView.Adapter<RecyclerHolder<E>>(),
    IListAdapter<E> {
    private val sparseArray = SparseArray<E>()
    override val events: ArrayList<IEvent<E>> = ArrayList()
    override val adapterList: MutableList<E> = ArrayList()
    private val event: IEvent<E> by lazy { this }
    override val array: SparseArray<Any> = SparseArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder<E> {
        return RecyclerHolder(parent, sparseArray.get(viewType).apply { event(event as IEvent<Any>) })
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onViewDetachedFromWindow(holder: RecyclerHolder<E>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewAttachedToWindow(holder: RecyclerHolder<E>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun getItemViewType(position: Int): Int {
        return adapterList[position].run {
            event(event as IEvent<Any>)
            layoutId().also { sparseArray.put(it, this) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerHolder<E>, position: Int) {
        holder.bindViewHolder(adapterList[position], event)
    }

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        when (type) {
            AdapterType.add -> notifyItemInserted(p)
            AdapterType.move -> notifyItemMoved(from, p)
            AdapterType.remove -> notifyItemRemoved(p)
            AdapterType.set -> notifyItemChanged(p)
            else -> notifyDataSetChanged()
        }
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        when (type) {
            AdapterType.add -> notifyItemRangeInserted(p, es.size)
            AdapterType.remove -> notifyItemRangeRemoved(p, es.size)
            AdapterType.set -> notifyItemRangeChanged(p, es.size)
            else -> notifyDataSetChanged()
        }
        return true
    }

    override fun onInserted(position: Int, count: Int) {
        notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        notifyItemRangeChanged(position, count, payload)
    }
}