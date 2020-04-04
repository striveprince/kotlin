package com.lifecycle.rx.adapter

import com.lifecycle.binding.IList
import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select

class RecyclerSelectAdapter<E : Select>(
    override val max: Int = Int.MAX_VALUE
) : RecyclerAdapter<E>(),
    IList<E>, ISelectList<E> {

    override val selectList: MutableList<E> = ArrayList()

}