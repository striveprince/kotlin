package com.binding.model.adapter.recycler

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.binding.model.adapter.IEventAdapter
import com.binding.model.adapter.IModelAdapter
import com.binding.model.adapter.IRecyclerAdapter
import com.binding.model.containsList
import com.binding.model.inflate.inter.Inflate

open class RecyclerAdapter<E : Inflate<out ViewDataBinding>> : RecyclerView.Adapter<RecyclerHolder<E>>()
    , IRecyclerAdapter<E> {
    private val sparseArray = SparseArray<E>()
    private val iEventAdapter by lazy { this }
    private val eventAdapters: ArrayList<IEventAdapter<E>> = ArrayList()

    init {
        eventAdapters.add(iEventAdapter)
    }

    override val holderList = ArrayList<E>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder<E> {
        return RecyclerHolder(parent, sparseArray.get(viewType))
    }

    override fun getItemCount(): Int {
        return holderList.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder<E>, position: Int) {
        val e = holderList[position]
        holder.executePendingBindings(e, iEventAdapter)
    }

    override fun onBindViewHolder(holder: RecyclerHolder<E>, position: Int, payloads: MutableList<Any>) {
//        if(payloads.isNotEmpty())
            onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        val e = holderList[position]
        val viewType = e.getLayoutId()
//        Timber.i("viewType=${viewType},Inflate=${e.javaClass.name}")
        sparseArray.put(viewType, e)
        return viewType
    }

    override fun size(): Int {
        return holderList.size
    }

    override fun clear() {
        val size = size()
        holderList.clear()
        notifyItemRangeRemoved(0, size)
    }


    override fun setEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        for (eventAdapter in eventAdapters) {
            if (eventAdapter is IModelAdapter<*>) {
                return setIEntity(position, e, type, view)
            } else if (eventAdapter.setEntity(position, e, type, view)) return true
        }
        return false
    }

    override fun addEventAdapter(eventAdapter: IEventAdapter<E>) {
        eventAdapters.add(0, eventAdapter)
    }

    override fun setListAdapter(position: Int, es: List<E>): Boolean {
        return when {
            position >= holderList.size -> addListAdapter(position, es)
            position + es.size >= holderList.size -> refreshListAdapter(position, es)
            else -> {
                es.forEachIndexed { index, e -> setToAdapter(index + position, e) }
                return true
            }
        }
    }

    override fun removeListAdapter(position: Int, es: List<E>): Boolean {
        val rang = isRang(position, es, holderList)
        if (rang >= 0) {
            holderList.removeAll(es)
            notifyItemRangeRemoved(position, es.size)
        } else for (e in es) {
            removeToAdapter(0, e)
        }

        return rang >= 0
    }


    private fun isRang(position: Int, es: List<E>, holderList: List<E>): Int {
        if (es.isEmpty()) return -2
        var rang = holderList.indexOf(es[0])
        for (i in es.indices) {
            if (holderList.indexOf(es[i]) == position + i) continue
            else {
                rang = -1
                break
            }
        }
        return rang
    }

    override fun addListAdapter(position: Int, es: List<E>): Boolean {
        var p = position
        if (!containsList(position, holderList)) {
            p = holderList.size
            holderList.addAll(es)
        } else holderList.addAll(p, es)
        notifyItemRangeInserted(p, es.size)
        return true
    }

    fun addListAdapter(es: List<E>): Boolean {
        return addListAdapter(0, es)
    }

    override fun refreshListAdapter(position: Int, es: List<E>): Boolean {
        if (position == holderList.size || holderList.isEmpty()) return addListAdapter(position, es)
        val eList = ArrayList<E>()
        if (containsList(position, holderList)) eList.addAll(0, holderList.subList(0, position))
        eList.addAll(es)
        notifyDataSetChanged()
        holderList.clear()
        holderList.addAll(eList)
        return true
    }

    override fun moveListAdapter(position: Int, es: List<E>): Boolean {
        for (i in es.indices)
            moveToAdapter(position + i, es[i])
        return isRang(position, es, holderList) >= 0
    }

    override fun setToAdapter(position: Int, e: E): Boolean {
        if (containsList(position, holderList)) {
            e.setEventAdapter(iEventAdapter)
            notifyItemChanged(position)
            holderList[position] = e
            return true
        }
        return false
    }

    override fun addToAdapter(position: Int, e: E): Boolean {
        var p = position
        if (!containsList(p, holderList)) {
            p = holderList.size
            notifyItemInserted(p)
            holderList.add(e)
        } else {
            notifyItemInserted(p)
            holderList.add(p, e)
        }
        return true
    }

    override fun removeToAdapter(position: Int, e: E): Boolean {
        var p = position
        if (holderList.contains(e))
            p = holderList.indexOf(e)
        else if (!containsList(p, holderList)) return false
        notifyItemRemoved(p)
        holderList.removeAt(p)
        return true
    }

    override fun moveToAdapter(position: Int, e: E): Boolean {
        var p = position
        if (p >= holderList.size) p = holderList.size - 1
        val from = holderList.indexOf(e)
        if (from >= 0 && from != p && holderList.remove(e)) {
            notifyItemMoved(from, p)
            holderList.add(p, e)
            return true
        }
        return false
    }
}