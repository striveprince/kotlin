package com.binding.model.adapter

import android.view.View
import com.binding.model.annoation.HandleEve
import io.reactivex.Single

interface IEventAdapter<E>{
    fun setEntity(position: Int, e: E, @HandleEve type: Int, view: View?): Single<EventEntity<*>>
}
