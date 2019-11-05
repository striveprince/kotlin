package com.binding.model.adapter

import android.view.View
import com.binding.model.inflate.inter.Inflate
import com.binding.model.inflate.obj.EventType.add
import com.binding.model.inflate.obj.EventType.move
import com.binding.model.inflate.obj.EventType.refresh
import com.binding.model.inflate.obj.EventType.remove
import com.binding.model.inflate.obj.EventType.set

interface IRecyclerAdapter<E : Inflate<*>> : IModelAdapter<E>, IEventAdapter<E> {
    fun addEventAdapter(eventAdapter: IEventAdapter<E>)

    fun setListAdapter(position: Int, es: List<E>): Boolean
    fun removeListAdapter(position: Int, es: List<E>): Boolean
    fun addListAdapter(position: Int, es: List<E>): Boolean
    fun refreshListAdapter(position: Int, es: List<E>): Boolean
    fun moveListAdapter(position: Int, es: List<E>): Boolean

    fun setToAdapter(position: Int, e: E): Boolean
    fun addToAdapter(position: Int, e: E): Boolean
    fun removeToAdapter(position: Int, e: E): Boolean
    fun moveToAdapter(position: Int, e: E): Boolean

    override fun setIEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        return when(type){
            add -> addToAdapter(position,e)
            set -> setToAdapter(position,e)
            remove -> removeToAdapter(position,e)
            move -> moveToAdapter(position,e)
            else -> false
        }
    }

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        return when(type){
            add -> addListAdapter(position,es)
            set -> setListAdapter(position,es)
            remove -> removeListAdapter(position,es)
            move -> moveListAdapter(position,es)
            refresh -> refreshListAdapter(position, es)
            else-> false
        }
    }
}