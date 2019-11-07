package com.binding.model.adapter

import android.view.View
import com.binding.model.annoation.HandleEvent
import io.reactivex.Single

interface IEventAdapter<E>{
    fun setEntity(position: Int, e: E, @HandleEvent type: Int, view: View?): Single<EventEntity<*>>
}
