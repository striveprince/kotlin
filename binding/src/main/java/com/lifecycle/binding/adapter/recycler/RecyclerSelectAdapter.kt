package com.lifecycle.binding.adapter.recycler

import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select

class RecyclerSelectAdapter<E : Select>(override val max: Int = Int.MAX_VALUE) : RecyclerAdapter<E>(), IListAdapter<E>, ISelectList<E> {
    override val selectList: MutableList<E> = ArrayList()
}