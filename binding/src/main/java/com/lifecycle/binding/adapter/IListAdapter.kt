package com.lifecycle.binding.adapter

import com.lifecycle.binding.annoation.HandleEvent

interface IListAdapter<E>{
    fun setList(position: Int, es: List<E>, @HandleEvent type: Int): Boolean
}
