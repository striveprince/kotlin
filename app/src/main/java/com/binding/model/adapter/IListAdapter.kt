package com.binding.model.adapter

import com.binding.model.annoation.HandleEve

interface IListAdapter<E>{
    fun setList(position: Int, e: List<E>, @HandleEve type: Int): Boolean
}
