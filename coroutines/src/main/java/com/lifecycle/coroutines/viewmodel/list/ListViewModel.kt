package com.lifecycle.coroutines.viewmodel.list

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.coroutines.IListAdapter
import com.lifecycle.coroutines.adapter.RecyclerAdapter
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import io.reactivex.Observable

open class ListViewModel<E : Inflate>(val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E> {
    var pageWay = true
    var pageCount = 10
    var headIndex = 0
    var offset = 0
    val loadingState= MutableLiveData(AdapterType.no)
    val error = MutableLiveData<Throwable>()
//    override val adapterList: MutableList<E> = adapter.adapterList

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        return adapter.notify(p, type, from)
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        return adapter.notifyList(p, type, es, from)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
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


    override fun clearList() {
        adapter.clearList()
    }

    override fun size(): Int {
        return adapter.size()
    }

    override val adapterList: MutableList<E> = adapter.adapterList

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
