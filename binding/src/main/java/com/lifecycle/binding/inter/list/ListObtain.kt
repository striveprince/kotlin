package com.lifecycle.binding.inter.list

import android.view.View
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.Obtain
import java.util.concurrent.atomic.AtomicBoolean

interface ListObtain<E,Job> :IListAdapter<E>, Obtain<List<E>, Job> {
    var pageWay:Boolean
    var pageCount :Int
    var headIndex :Int
    var offset:Int
    var job: Job?
    val adapter: IListAdapter<E>
    val canRun: AtomicBoolean

    fun start(@AdapterEvent state: Int)

    fun getData(state: Int)

    fun getStartOffset(state: Int): Int {
        offset = if (state.stateEqual(AdapterType.refresh)) 0 else size()
        val p = if (offset > headIndex) offset - headIndex else 0
        return if (pageWay) p / pageCount + 1 else p
    }

    fun getEndOffset(state: Int): Int {
        val position = if(pageWay)(offset-headIndex)/pageCount*pageCount else offset
        val headIndex = if(state.stateEqual(AdapterType.refresh)) 0 else this.headIndex
        return position+headIndex
    }

    override fun onComplete() {
        canRun.compareAndSet(false,true)
    }



    override fun notify(p: Int, type: Int, from: Int): Boolean {
        return adapter.notify(p, type, from)
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        return adapter.notifyList(p, type, es, from)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        return adapter.setEvent(type, e, position, view)
    }

    override fun addEventAdapter(event: (Int, E, Int, View?) -> Any) {
        adapter.addEventAdapter(event)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.onInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.onRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.onMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.onChanged(position, count, payload)
    }

    override fun setIEntity(e: E, position: Int, type: Int, view: View?): Boolean {
        return adapter.setIEntity(e, position, type, view)
    }

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        return adapter.setList(position, es, type)
    }

    override fun add(e: E, position: Int): Boolean {
        return adapter.add(e, position)
    }

    override fun set(e: E, position: Int): Boolean {
        return adapter.set(e, position)
    }

    override fun move(e: E, position: Int): Boolean {
        return adapter.move(e, position)
    }

    override fun remove(e: E): Boolean {
        return adapter.remove(e)
    }

    override fun removeAt(position: Int): Boolean {
        return adapter.removeAt(position)
    }

    override fun addList(es: List<E>, position: Int): Boolean {
        return adapter.addList(es, position)
    }

    override fun refreshList(es: List<E>, position: Int): Boolean {
        return adapter.refreshList(es, position)
    }

    override fun set(es: List<E>, position: Int): Boolean {
        return adapter.set(es, position)
    }
//
//    override fun moveList(position: Int, from: Int, size: Int): Boolean {
//        return adapter.moveList(position, from, size)
//    }

    override fun moveList(position: Int, es: List<E>): Boolean {
        return adapter.moveList(position, es)
    }

    override fun removeList(es: List<E>): Boolean {
        return adapter.removeList(es)
    }

    override fun removeList(from: Int, size: Int): Boolean {
        return adapter.removeList(from,size)
    }

    override fun addEventAdapter(event: IEvent<E>) {
        adapter.addEventAdapter(event)
    }

    override fun clearList() {
        adapter.clearList()
    }

    override fun size(): Int {
        return adapter.size()
    }

    fun running(state:Int):Boolean{
        return isStateRunning(state)
    }

    fun onCancelClick(v:View){
        onComplete()
    }

    fun isSuccess(state: Int):Boolean{
        return isStateSuccess(state)
    }
}