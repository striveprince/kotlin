package com.lifecycle.binding.inter

import android.view.View
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.event.IListAdapter
import timber.log.Timber
import kotlin.math.min


interface ISelectList<E : Select> : IListAdapter<E> {
    val selectList: MutableList<E>
    val max: Int

    override fun setIEntity(e: E, position: Int, type: Int, view: View?): Boolean {
        return if (type == AdapterType.select) select(e, position)
        else super.setIEntity(e, position, type, view).apply { asyncEntity(e, type) }
    }

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        return if (type == AdapterType.select) select(es, position)
        else super.setList(position, es, type).apply { firstAsync(type) }
    }

    fun select(es: List<E>, position: Int): Boolean {
        var b = true
        es.forEach { e -> if (!select(e)) b = false }
        return b
    }

    fun select(e: E, position: Int = -1): Boolean {
        val p = if (position < 0) adapterList.indexOf(e) else position
        if (adapterList[p] != e) throw RuntimeException("this e is not the same with index of adapterList")
        return selectStatus(e, !(selectList.contains(e).also { e.select(it) }))
    }

    fun selectStatus(e: E, check: Boolean): Boolean {
        selectList.asyncList()
        if (e.isSelected() == check) return check
        return if (check) {
            if (e.isPush()) selectList.add(e)
                .also { e.select(it) }
                .apply { selectList.asyncList() }
            else add(e)
        } else {
            if (!e.couldTakeBack()) e.select(true)
            else selectList.remove(e).let { e.select(false) }
        }
    }

    private fun add(inE: E): Boolean {
        Timber.i("add selectList.size = ${selectList.size} max = $max")
        return selectList.size < max && selectList.add(inE).let { inE.select(it) }
    }

    fun firstAsync(type: Int) {
        selectList.clear()
        adapterList.forEach { e -> if (e.isSelected())add(e) }
        when (type) { AdapterType.refresh, AdapterType.remove -> selectList.asyncList() }
    }

    fun asyncEntity(e: E, type: Int) {
        when (type) {
            AdapterType.remove -> selectList.remove(e).also { if(it)e.select(false) }
            AdapterType.add -> selectStatus(e,e.isSelected())
        }
    }

    fun MutableList<E>.asyncList() {
        while (max in indices) removeAt(0).select(false)
    }

    fun selectList(list:List<E> = adapterList,boolean: Boolean = false){
        for (e in list) selectStatus(e,boolean)
    }

    private fun E.isPush() = checkWay and 1 == 1

    private fun E.couldTakeBack(): Boolean = checkWay shr 1 and 1 == 1
}