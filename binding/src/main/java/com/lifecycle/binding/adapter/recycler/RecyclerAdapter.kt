package com.lifecycle.binding.adapter.recycler

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate

open class RecyclerAdapter<E : Inflate> : RecyclerView.Adapter<RecyclerHolder<E>>(), IListAdapter<E> {
    private val sparseArray = SparseArray<E>()
    override val events: ArrayList<IEvent<E>> = ArrayList()
    override val adapterList: MutableList<E> = ArrayList()
    private val event: IEvent<E> by lazy { this }
    override val tag: SparseIntArray = SparseIntArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder<E> {
        return RecyclerHolder(parent, sparseArray.get(viewType))
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
        val e = adapterList[position]
        val viewType = e.layoutId()
        sparseArray.put(viewType, e)
        return viewType
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