package com.lifecycle.rx.viewmodel.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.IEvent
import com.lifecycle.rx.IListAdapter
import com.lifecycle.rx.adapter.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.binding.util.observer
import com.lifecycle.rx.util.ioToMainThread
import com.lifecycle.rx.viewmodel.LifeViewModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable

open class ListViewModel<E : Inflate>(val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, Observer<MutableList<E>> {
    var pageWay = true
    var pageCount = 10
    var headIndex = 0
    var offset = 0
    val loadingState= MutableLiveData(AdapterType.no)
    val error = MutableLiveData<Throwable>()
    override val adapterList: MutableList<E> = adapter.adapterList
    private var disposable: Disposable? = null
    var enable = MutableLiveData<Boolean>(true)
    var httpData :(Int,Int)->Single<List<E>> = {_,_->Single.just(ArrayList())}
    override fun attachData(owner: LifecycleOwner,  bundle: Bundle?) {
        super.attachData(owner, bundle)
        loadingState.observer(owner) { doGetData(it) }
        loadingState.value = AdapterType.refresh
    }

    private fun doGetData(it:Int){
//        Timber.i("it=$it")
        if (it!=0 && enable.value!!) {
            enable.value = false
            httpData(getStartOffset(), loadingState.value!!)
                .ioToMainThread()
                .doFinally { enable.value = true }
                .doFinally { loadingState.value = AdapterType.no}
                .map { if(it is ArrayList)it else ArrayList(it) }
                .subscribe(NormalObserver(this))
        }
    }


    override fun onNext(t: MutableList<E>) {
        setList(getEndOffset(), t, loadingState.value!!)
    }

    override fun onComplete() {
        disposable?.dispose()
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }


    override fun onError(e: Throwable) {
        error.value = e
    }

    private fun getStartOffset(): Int {
        offset = if (loadingState.value == AdapterType.refresh) 0 else size()
        val p = if (offset > headIndex) offset - headIndex else 0
        return if (pageWay) p / pageCount + 1 else p
    }

    private fun getEndOffset(): Int {
        val position = if(pageWay)(offset-headIndex)/pageCount*pageCount else offset
        val headIndex = if(loadingState.value == AdapterType.refresh) 0 else this.headIndex
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

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
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


    override fun addEventAdapter(event: IEvent<E, Observable<Any>>) {
        adapter.addEventAdapter(event)
    }

    override fun clearList() {
        adapter.clearList()
    }

    override fun size(): Int {
        return adapter.size()
    }
}