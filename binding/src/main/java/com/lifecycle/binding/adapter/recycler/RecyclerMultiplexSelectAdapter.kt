package com.lifecycle.binding.adapter.recycler

import com.lifecycle.binding.inter.*

class RecyclerMultiplexSelectAdapter<E : MultiplexSelect>() : RecyclerAdapter<E>(), ISelectMultiplexList<E> {
    override val selectList: MutableList<E> = ArrayList()
    override val selectMap: MutableMap<String, SelectType<E>> = HashMap()
}