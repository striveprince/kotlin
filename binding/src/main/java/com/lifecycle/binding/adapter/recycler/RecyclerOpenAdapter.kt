package com.lifecycle.binding.adapter.recycler

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.event.IEvent
import com.lifecycle.binding.adapter.inter.IList
import com.lifecycle.binding.inter.inflate.Inflate

abstract class RecyclerOpenAdapter<E : Inflate,R> : RecyclerView.Adapter<RecyclerHolder<E,R>>(), IList<E, R> {
    private val sparseArray = SparseArray<E>()

    private val event: IEvent<E,R> by lazy { this }
    val events: ArrayList<IEvent<E,R>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder<E,R> {
        return RecyclerHolder(parent, sparseArray.get(viewType))
    }

//    override fun setEvent(position: Int, e: E, type: Int, view: View?): R {
//        for (event in eventObservableEvents) return event.setEvent(position, e, type, view)
//        return Observable.just(setIEntity(e,position, type, view))
//    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onViewDetachedFromWindow(holder: RecyclerHolder<E,R>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewAttachedToWindow(holder: RecyclerHolder<E,R>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun getItemViewType(position: Int): Int {
        val e = adapterList[position]
        val viewType = e.layoutId()
        sparseArray.put(viewType, e)
        return viewType
    }

    override fun onBindViewHolder(holder: RecyclerHolder<E,R>, position: Int) {
        holder.bindViewHolder(adapterList[position], event)
    }

    override fun addEventAdapter(event: IEvent<E, R>) {
        events.add(0,event)
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