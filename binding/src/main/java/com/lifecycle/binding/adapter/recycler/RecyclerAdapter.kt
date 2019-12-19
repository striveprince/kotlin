package com.lifecycle.binding.adapter.recycler

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.IEvent
import com.lifecycle.binding.adapter.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

open class RecyclerAdapter<E : Inflate> : RecyclerView.Adapter<RecyclerHolder<E>>(), IListAdapter<E> {
    override val adapterList = ArrayList<E>()
    private val sparseArray = SparseArray<E>()

    private val iEvent: IEvent<E> by lazy { this }
    private val events: ArrayList<IEvent<E>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder<E> {
        return RecyclerHolder(parent, sparseArray.get(viewType))
    }

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e,position, type, view))
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
        holder.bindViewHolder(adapterList[position], iEvent)
    }

    override fun addEventAdapter(event: IEvent<E>) {
        events.add(0, event)
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