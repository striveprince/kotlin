package com.lifecycle.binding.inter.event

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.stateOriginal


interface IListAdapter<E> : IEvent<E>, ListUpdateCallback {

    val adapterList: MutableList<E>
    val events: ArrayList<IEvent<E>>
    val array: SparseArray<Any>
    fun addEventAdapter(event: IEvent<E>) {
        events.add(0, event)
    }

    fun addEventAdapter(event: (Int, E, Int, View?) -> Any) {
        addEventAdapter(object : IEvent<E> {
            override fun setEvent(type: Int, e: E, position: Int, view: View?) = event(type, e, position, view)
        })
    }

    fun clearList() {
        adapterList.clear()
        notifyList(AdapterType.no, 0, adapterList)
    }

    fun size(): Int = adapterList.size

    fun setIEntity(it: Event<E>) = setIEntity(it.e, it.position, it.type, it.view)

    fun setIEntity(e: E, position: Int, @AdapterEvent type: Int, view: View?): Boolean {
        return when (stateOriginal(type)) {
            AdapterType.add, AdapterType.load -> add(e, position)
            AdapterType.set -> set(e, position)
            AdapterType.remove -> if (remove(e)) true else removeAt(position)
            AdapterType.move -> move(e, position)
            else -> false
        }
    }

    fun setList(it: Event<List<E>>) = setList(it.position, it.e, it.type)

    fun setList(position: Int, es: List<E>, type: Int): Boolean {
        return when (stateOriginal(type)) {
            AdapterType.add -> addList(es, position)
            AdapterType.move -> moveList(position, es)
            AdapterType.remove -> removeList(es)
            AdapterType.refresh, AdapterType.load -> refreshList(es, position)
            AdapterType.set -> set(es, position)
            else -> false
        }
    }

    fun add(e: E, position: Int = -1): Boolean {
        val p = adapterList.run { if (position in indices) position.also { add(position, e) } else add(e).run { lastIndex } }
        return notify(AdapterType.add, p)
    }

    fun set(e: E, position: Int): Boolean {
        return if (position in adapterList.indices) {
            adapterList[position] = e
            notify(AdapterType.set, position)
        } else false
    }

    fun move(e: E, position: Int): Boolean {
        if (position !in adapterList.indices) return false
        val from = adapterList.indexOf(e)
        return if (from in adapterList.indices && from != position && adapterList.remove(e)) {
            adapterList.add(position, e)
            notify(AdapterType.move, position, from)
        } else false
    }

    fun remove(e: E): Boolean {
        val position = adapterList.indexOf(e)
        return if (position in adapterList.indices) removeAt(position)
        else false
    }

    fun removeAt(position: Int): Boolean {
        if (position in adapterList.indices){
            adapterList.removeAt(position)
            notify(AdapterType.remove, position)
        }
        return false
    }

    fun addList(es: List<E>, position: Int = Int.MAX_VALUE): Boolean {
        val p = if (position in adapterList.indices) position else adapterList.size
        adapterList.addAll(p, es)
        return notifyList(AdapterType.add, position, es)
    }

    fun moveList(position: Int, es: List<E>): Boolean {
        if (es.isNotEmpty()) {
            val from = adapterList.indexOf(es.first())
            val size = es.size
            if (from < 0) return false
            val list = ArrayList(adapterList.subList(from, size))
            adapterList.removeAll(list)
            val p = if (position in adapterList.indices) position else adapterList.size
            adapterList.addAll(p, list)
            return notifyList(AdapterType.move, p, list)
        }
        return false
    }

    fun removeList(from: Int, size: Int): Boolean {
        val list = ArrayList(adapterList.subList(from, from + size))
        adapterList.removeAll(list)
        return notifyList(AdapterType.remove, from, list)
    }

    fun removeList(es: List<E>): Boolean {
        if (es.isEmpty()) return false
        val position = adapterList.indexOf(es.first())
        if (position < 0) return false
        val list = ArrayList(adapterList.subList(position, position + es.size))
        return if (list.checkEqualList(es)) removeList(position, es.size)
        else false.also { es.forEach { remove(it) } }
    }

    fun List<E>.checkEqualList(es: List<E>): Boolean {
        if (size != es.size) return false
        es.forEachIndexed { index, e ->
            val ie = get(index)
            if (e is Diff && ie is Diff && e.key() != ie.key()) return false
            else if (ie != e) return false
        }
        return true
    }


    fun refreshList(es: List<E>, position: Int = 0): Boolean {
        return if (position in adapterList.indices) {
            if (position == 0) adapterList.clear()
            else while (position in adapterList.indices) adapterList.removeAt(adapterList.lastIndex)
            adapterList.addAll(position, es)
            notifyList(AdapterType.refresh, position, es)
        } else {
            addList(es, position)
        }
    }

    fun set(es: List<E>, position: Int): Boolean {
        if (position in adapterList.indices && (position + es.size in adapterList.indices)) {
            for (index in es.indices) {
                adapterList[index + position] = es[index]
            }
            notifyList(AdapterType.set, position, es)
            return true
        }
        return false
    }

    fun notify(type: Int, p: Int, from: Int = 0): Boolean {
        notifyDataSetChanged()
        return true
    }

    fun notifyList(type: Int, p: Int, es: List<E>, from: Int = 0): Boolean {
        notifyDataSetChanged()
        return true
    }

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
