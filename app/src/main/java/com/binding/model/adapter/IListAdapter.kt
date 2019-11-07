package com.binding.model.adapter

import com.binding.model.annoation.HandleEvent

interface IListAdapter<E>{
    fun setList(position: Int, es: List<E>, @HandleEvent type: Int): Boolean
}
