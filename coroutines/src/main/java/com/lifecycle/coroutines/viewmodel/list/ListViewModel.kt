package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.inter.list.ListModel
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.coroutines.util.HttpData
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, ListModel<E, Job> {
    override val tag: SparseArray<Any> = SparseArray()
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(stateStart(AdapterType.refresh))
    override val error = MutableLiveData<Throwable>()
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    var httpData: HttpData<E> = { _, _: Int -> flow { emit(ArrayList<E>()) } }
    override val canRun: AtomicBoolean = AtomicBoolean(true)
    override val events: ArrayList<IEvent<E>> = adapter.events


    @ExperimentalCoroutinesApi
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) { if (isStateStart(it) && canRun.getAndSet(false)) getData(it) }
    }

    @ExperimentalCoroutinesApi
    open fun doGetData(state: Int) {

    }

    override fun onSubscribe(job: Job) {
        this.job = job
        addJob(job)
    }

    override fun getData(state: Int) {
        onSubscribe(launchUI {
            httpData(getStartOffset(state), state)
                .flowOn(Dispatchers.IO)
                .catch { onError(it) }
                .onCompletion { onComplete() }
                .collect { onNext(it) }
        })
    }
}
