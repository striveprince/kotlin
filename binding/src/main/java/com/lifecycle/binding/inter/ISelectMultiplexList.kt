package com.lifecycle.binding.inter

import android.view.View
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterType

interface ISelectMultiplexList<E : MultiplexSelect> : IListAdapter<E> {
    val selectList: MutableList<E>
    val selectMap: MutableMap<String, SelectType<E>>

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
        es.forEachIndexed { _, e -> if (!select(e)) b = false }
        return b
    }

    fun select(e: E, position: Int = -1): Boolean {
        val p = if (position < 0) adapterList.indexOf(e) else position
        if (adapterList[p] != e) throw RuntimeException("this e is not the same with index of adapterList")
        return selectStatus(e, !(selectMap.contain(e, selectList).also { e.select(it) }))
    }

    fun selectStatus(e: E, check: Boolean): Boolean {
        if (e.isSelected() == check && selectMap.contain(e, selectList) == check) return check
        return if (!check) {
            if (!e.couldTakeBack()) {
                asyncSelectMap(e, true)
            } else selectMap.remove(e, selectList).let { e.select(false) }
        } else {
            if (e.isPush()) selectMap.add(e, selectList)
                .apply { asyncSelectMap(e, this) }
            else selectMap.size(e) < e.max() && selectMap.add(e, selectList).let { e.select(it) }
        }
    }

    fun asyncSelectMap(e: E, b: Boolean): Boolean {
        return e.select(b).apply {
            while (selectMap.size(e) > e.max())
                selectMap.removeAt(0, selectList, e.selectType()).select(false)
        }
    }

    fun firstAsync(type: Int) {
        when (type) {
            AdapterType.refresh, AdapterType.remove -> {
                selectMap.clear(selectList)
                adapterList.reversed().forEach {
                    val s = it.isSelected() && selectMap.size(it) < it.selectMax()
                    if (s) selectMap.add(it, selectList)
                    it.select(s)
                }
            }
        }
    }

    fun asyncEntity(e: E, type: Int) {
        when (type) {
            AdapterType.remove -> selectMap.remove(e, selectList).also { if (it) e.select(false) }
            AdapterType.add -> selectStatus(e, e.isSelected())
        }
    }

    fun selectList(list: List<E> = adapterList, boolean: Boolean = false) {
        if (adapterList == list && !boolean) {
            selectMap.clear(selectList)
            for (e in adapterList) e.select(false)
        } else
            for (e in list) selectStatus(e, boolean)
    }

    private fun E.isPush() = checkWay and 1 == 1

    private fun E.couldTakeBack(): Boolean = checkWay shr 1 and 1 == 1

    private fun E.max() = selectMap[selectType()]?.max ?: Int.MAX_VALUE

    fun Map<String, SelectType<E>>.contain(e: E, list: List<E>): Boolean {
        return list.contains(e)
    }

    fun Map<String, SelectType<E>>.size(e: E): Int {
        return get(e.selectType())?.selects?.size ?: 0
    }

    fun Map<String, SelectType<E>>.remove(e: E, list: MutableList<E>): Boolean {
        return list.remove(e)
            .also { get(e.selectType())?.selects?.remove(e) }
    }

    fun Map<String, SelectType<E>>.removeAt(index: Int, list: MutableList<E>, selectType: String): E {
        return get(selectType)?.selects?.removeAt(index)?.also { list.remove(it) } ?: list.removeAt(index)
    }

    fun Map<String, SelectType<E>>.lastIndex(e: E): Int {
        return get(e.selectType())?.selects?.lastIndex ?: 0
    }

    fun MutableMap<String, SelectType<E>>.clear(list: MutableList<E>) {
        clear()
        list.clear()
    }

//    fun MutableMap<String, SelectType<E>>.asyncList(list: MutableList<E>) {
//        clear()
//        for (e in list) selectStatus(e, e.isSelected())
//    }

    fun MutableMap<String, SelectType<E>>.add(inE: E, list: MutableList<E>): Boolean {
        return list.add(inE).apply {
            if (get(inE.selectType())?.selects?.add(inE) == null)
                put(inE.selectType(), SelectType<E>(inE.selectMax()).apply { selects.add(inE) })
        }
    }
}


class SelectType<E>(val max: Int, val selects: ArrayList<E> = ArrayList())

interface MultiplexSelect : Select {
    fun selectType() = javaClass.simpleName

    fun selectMax() = Int.MAX_VALUE
}