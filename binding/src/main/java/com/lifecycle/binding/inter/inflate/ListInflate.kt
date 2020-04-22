package com.lifecycle.binding.inter.inflate

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.Obtain
import java.util.concurrent.atomic.AtomicBoolean

interface ListInflate<E, Binding:ViewDataBinding, Job> : IListAdapter<E>, Obtain<List<E>, Job>,BindingInflate<Binding> {
    var pageWay: Boolean
    var pageCount: Int
    var headIndex: Int
    var offset: Int
    val loadingState: ObservableInt
    val error: ObservableField<Throwable>
    var job: Job?
    val adapter: IListAdapter<E>
    val canRun:AtomicBoolean
    var callback : Observable.OnPropertyChangedCallback?

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView)
            .apply {
                callback = loadingState.observe { getData(it) }
                loadingState.set(stateStart(AdapterType.refresh))
            }
    }

    fun getData(state:Int)

    override fun onNext(t: List<E>) {
        setList(getEndOffset(loadingState.get()), t, loadingState.get())
        loadingState.set(stateSuccess(loadingState.get()))
    }

    override fun onSubscribe(job: Job) {
        this.job = job
    }

    override fun onError(e: Throwable) {
        error.set(e)
        loadingState.set(stateError(loadingState.get()))
    }

    override fun onComplete() {
        loadingState.set(stateEnd(loadingState.get()))
    }


    fun getStartOffset(state: Int): Int {
        offset = if (state.stateEqual(AdapterType.refresh)) 0 else size()
        val p = if (offset > headIndex) offset - headIndex else 0
        return if (pageWay) p / pageCount + 1 else p
    }

    fun getEndOffset(state: Int): Int {
        val position = if (pageWay) (offset - headIndex) / pageCount * pageCount else offset
        val headIndex = if (state.stateEqual(AdapterType.refresh)) 0 else this.headIndex
        return position + headIndex
    }

    fun destroy(){
        callback?.let { loadingState.removeOnPropertyChangedCallback(it) }
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

    fun running(state: Int): Boolean {
        return isStateRunning(state)
    }

    fun onCancelClick(v:View){
        onComplete()
    }

    fun isSuccess(state :Int):Boolean{
        return state shr 8 == 1
    }
}