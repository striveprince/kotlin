package com.binding.model.adapter

import com.binding.model.annoation.HandleEve

interface IListAdapter<E>{
    fun setList(position: Int, es: List<E>, @HandleEve type: Int): Boolean
}
