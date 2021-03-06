package com.lifecycle.binding.adapter.recycler

import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select

class RecyclerSelectAdapter<E : Select>(override var max: Int = Int.MAX_VALUE) : RecyclerAdapter<E>(), ISelectList<E> {
    override val selectList: MutableList<E> = ArrayList()
}