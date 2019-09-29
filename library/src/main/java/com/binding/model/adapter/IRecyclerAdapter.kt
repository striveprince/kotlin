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
        when(type){
            add -> return addToAdapter(position,e)
            set -> return setToAdapter(position,e)
            remove -> return removeToAdapter(position,e)
            move -> return moveToAdapter(position,e)
        }
        return false
    }

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        when(type){
            add -> return addListAdapter(position,es)
            set -> return setListAdapter(position,es)
            remove -> return removeListAdapter(position,es)
            move -> return moveListAdapter(position,es)
            refresh -> return refreshListAdapter(position, es)
        }
        return false
    }
}