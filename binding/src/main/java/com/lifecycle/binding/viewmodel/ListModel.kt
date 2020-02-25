package com.lifecycle.binding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*

interface ListModel<E,R,Job>: IList<E,R>,Obtain<MutableList<E>,Job> {
    var pageWay:Boolean
    var pageCount :Int
    var headIndex :Int
    var offset:Int
    val loadingState : MutableLiveData<Int>
    val error : MutableLiveData<Throwable>
    var job: Job?
    val adapter:IList<E,R>


    override fun onNext(t: MutableList<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, it)
            loadingState.value = stateSuccess(it)
        }
    }

    override fun onError(e: Throwable) {
        error.value = e
        loadingState.value = stateError(loadingState.value!!)
    }

    override fun onComplete() {
        loadingState.value = stateEnd(loadingState.value!!)
    }

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

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        return adapter.notify(p, type, from)
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        return adapter.notifyList(p, type, es, from)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun setEvent(position: Int, e: E, type: Int, view: View?): R {
        return adapter.setEvent(position, e, type, view)
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

    override fun setList(position: Int, es: MutableList<E>, type: Int): Boolean {
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

    override fun remove(e: E, position: Int): Boolean {
        return adapter.remove(e, position)
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

    override fun moveList(position: Int, from: Int, size: Int): Boolean {
        return adapter.moveList(position, from, size)
    }

    override fun removeList(position: Int, from: Int, size: Int): Boolean {
        return adapter.removeList(position, from, size)
    }


    override fun addEventAdapter(event: IEvent<E, R>) {
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
}