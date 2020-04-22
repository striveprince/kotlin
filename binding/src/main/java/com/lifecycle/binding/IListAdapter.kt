package com.lifecycle.binding

import android.view.View
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.stateOriginal


interface IListAdapter<E> : IEvent<E>, ListUpdateCallback {

    val adapterList: MutableList<E>
    val events: ArrayList<IEvent<E>>
    fun addEventAdapter(event: IEvent<E>) {}

    fun addEventAdapter(event: (Int, E, Int, View?) -> Any) {
        addEventAdapter(object : IEvent<E> {
            override fun setEvent(type: Int, e: E, position: Int, view: View?) = event(type, e, position, view)
        })
    }

    fun clearList() {
        adapterList.clear()
        notifyList(0, AdapterType.remove, adapterList)
    }

    fun size(): Int = adapterList.size
    fun setIEntity(e: E, position: Int, @AdapterEvent type: Int, view: View?): Boolean {
        return when (stateOriginal(type)) {
            AdapterType.add, AdapterType.load -> add(e, position)
            AdapterType.set -> set(e, position)
            AdapterType.remove -> remove(e, position)
            AdapterType.move -> move(e, position)
            else -> false
        }
    }

    fun setList(position: Int, es: List<E>, type: Int): Boolean {
        if (es.isEmpty()) return false
        return when (stateOriginal(type)) {
            AdapterType.add -> addList(es, position)
            AdapterType.move -> moveList(position, adapterList.indexOf(es.first()), es.size)
            AdapterType.remove -> removeList(es)
            AdapterType.refresh, AdapterType.load -> refreshList(es, position)
            AdapterType.set -> set(es, position)
            else -> false
        }
    }

    fun add(e: E, position: Int = -1): Boolean {
        val p = if (position in adapterList.indices) {
            adapterList.add(position, e)
            position
        } else {
            adapterList.add(e)
            adapterList.lastIndex
        }
        return notify(p, AdapterType.add)
    }

    fun set(e: E, position: Int): Boolean {
        return if (position in adapterList.indices) {
            adapterList[position] = e
            notify(position, AdapterType.set)
        } else false
    }

    fun move(e: E, position: Int): Boolean {
        val p = if (position in adapterList.indices) position else adapterList.lastIndex
        val from = adapterList.indexOf(e)
        return if (from >= 0 && from != p && adapterList.remove(e)) {
            adapterList.add(p, e)
            notify(p, AdapterType.move, from)
        } else false
    }

    fun remove(e: E, position: Int = -1): Boolean {
        return if (position in adapterList.indices) {
            adapterList.remove(e)
            notify(position, AdapterType.remove)
        } else false
    }

    fun addList(es: List<E>, position: Int = Int.MAX_VALUE): Boolean {
        val p = if (position in adapterList.indices) position else adapterList.size
        adapterList.addAll(p, es)
        return notifyList(position, AdapterType.add, es)
    }

    fun moveList(position: Int, from: Int, size: Int): Boolean {
        if (from < 0) return false
        val list = ArrayList(adapterList.subList(from, size))
        adapterList.removeAll(list)
        val p = if (position in adapterList.indices) position else adapterList.size
        adapterList.addAll(p, list)
        return notifyList(p, AdapterType.move, list)
    }

    fun removeList(from: Int,size: Int):Boolean{
        val list = ArrayList(adapterList.subList(from,from+size))
        adapterList.removeAll(list)
        return notifyList(from, AdapterType.remove, list)
    }

    fun removeList(es: List<E>): Boolean {
        if (es.isEmpty()) return false
        val position = adapterList.indexOf(es.first())
        if (position < 0) return false
        val list = ArrayList(adapterList.subList(position, position+es.size))
        if (checkEqualList(list, es)) {
            return removeList(position,es.size)
        } else es.forEach { remove(it) }
        return false
    }

    fun checkEqualList(list: List<E>, es: List<E>): Boolean {
        es.forEachIndexed { index, e ->
            val ie = list[index]
             if(e is Diff && ie is Diff){
                if(e.key() != ie.key())return false
            }else if (ie != e) return false
        }
        return true
    }


    fun refreshList(es: List<E>, position: Int = 0): Boolean {
        return if (position in adapterList.indices) {
            if (position == 0) adapterList.clear()
            else adapterList.removeAll(adapterList.subList(position, size()))
            adapterList.addAll(position, es)
            notifyList(position, AdapterType.refresh, es)
        } else {
            addList(es, position)
        }
    }

    fun set(es: List<E>, position: Int): Boolean {
        if (position in adapterList.indices && (position + es.size in adapterList.indices)) {
            for (index in es.indices) {
                adapterList[index + position] = es[index]
            }
            notifyList(position, AdapterType.set, es)
            return true
        }
        return false
    }

    fun notify(p: Int, type: Int, from: Int = 0): Boolean

    fun notifyList(p: Int, type: Int, es: List<E>, from: Int = 0): Boolean

    fun notifyDataSetChanged()

    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        for (event in events) event.setEvent(type, e, position, view).let { if (it != false) return it }
        return setIEntity(e, position, type, view)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {

    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {

    }

    override fun onInserted(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {
    }
}
