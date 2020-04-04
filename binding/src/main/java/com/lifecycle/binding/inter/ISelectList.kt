package com.lifecycle.binding.inter

import android.view.View
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import kotlin.math.min


interface ISelectList<E : Select> : IList<E> {
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
        es.forEachIndexed { _, e -> if (!select(e)) b = false }
        return b
    }

    fun select(e: E, position: Int = -1): Boolean {
        val p = if (position < 0) adapterList.indexOf(e) else position
        if (adapterList[p] != e) throw RuntimeException("this e is not the same with index of adapterList")
        return selectStatus(e, !(selectList.contains(e).let { e.check(it) }))
    }

    fun selectStatus(e: E, check: Boolean): Boolean {
        asyncList()
        if (e.isChecked == check) return check
        return if (!check) {
            if (!e.couldTakeBack()) e.check(true)
            else selectList.remove(e).let { e.check(false) }
        } else {
            if (e.isPush()) selectList.add(e)
                .apply { while (selectList.size > max) selectList.removeAt(0).check(false) }
                .let { e.check(it) }
            else add(e)
        }
    }

    private fun add(inE: E): Boolean {
        return selectList.size < max && selectList.add(inE).let { inE.check(it) }
    }

    fun firstAsync(type: Int) {
        when (type) {
            AdapterType.refresh, AdapterType.remove -> {
                selectList.clear()
                adapterList.forEach { e -> e.check(e.isChecked).also { if (it) selectList.add(e) } }
            }
        }
        asyncList()
    }

    fun asyncEntity(e: E, type: Int) {
        when (type) {
            AdapterType.remove -> selectList.remove(e).run { e.check(false) }
            AdapterType.add -> selectStatus(e,e.isChecked)
        }
    }

    fun asyncList() {
        if (selectList.size > max) {
            val arrayList = ArrayList<E>()
            for (it in selectList) if (it.isPush()) arrayList.add(it)
            val l = selectList.size - max
            for (index in 0..min(l, arrayList.size)) {
                selectList.remove(arrayList[index])
                arrayList[index].check(false)
            }
            if (l > arrayList.size)
                for (index in 0..l - arrayList.size)
                    selectList.removeAt(selectList.lastIndex).check(false)
        }
    }

    private fun E.isPush() = checkWay and 1 == 1

    private fun E.couldTakeBack(): Boolean = checkWay shr 1 and 1 == 1
}