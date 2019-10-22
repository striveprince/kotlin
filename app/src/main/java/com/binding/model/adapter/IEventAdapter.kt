package com.binding.model.adapter

import android.view.View
import com.binding.model.annoation.HandleEve

interface IEventAdapter<E>{
    fun setEntity(position: Int, e: E, @HandleEve type: Int, view: View?): Boolean
}
