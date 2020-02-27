package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.stateError
import com.lifecycle.binding.util.stateStart
import com.lifecycle.binding.util.stateSuccess
import com.lifecycle.binding.viewmodel.ListModel
import com.lifecycle.coroutines.IListAdapter
import com.lifecycle.coroutines.adapter.RecyclerAdapter
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, ListModel<E, Any, Job> ,HttpData<List<E>>{
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override val error = MutableLiveData<Throwable>()
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    override var canRun: AtomicBoolean = AtomicBoolean(true)
    var http: HttpData<List<E>> = this

    override suspend fun require(startOffset: Int, it: Int) = ArrayList<E>()

    @ExperimentalCoroutinesApi
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) { doGetData(it) }
        loadingState.value = stateStart(AdapterType.refresh)
    }

    @ExperimentalCoroutinesApi
    open fun doGetData(it: Int) {
        if (it != 0 && canRun.getAndSet(false)) {
            onSubscribe(launchUI {
                flow { emit(http.require(getStartOffset(it), it)) }
                    .flowOn(Dispatchers.IO)
                    .catch{ onError(it) }
                    .onCompletion { onComplete() }
                    .collect{ onNext(it) }
            })
        }
    }

    override fun onSubscribe(job: Job) {
        this.job = job
        addJob(job)
    }

    override fun onNext(t: List<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, it)
            loadingState.value = stateSuccess(it)
        }
    }

    override fun onError(e: Throwable) {
        error.value = e
        loadingState.value = stateError(loadingState.value!!)
    }


}
