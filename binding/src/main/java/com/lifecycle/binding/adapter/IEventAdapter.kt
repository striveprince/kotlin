package com.lifecycle.binding.adapter

import android.view.View
import com.lifecycle.binding.annoation.HandleEvent
import io.reactivex.Single

interface IEventAdapter<E>{
    fun setEntity(position: Int, e: E, @HandleEvent type: Int, view: View?): Single<EventEntity<*>>
}
