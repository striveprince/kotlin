package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.ListModel
import com.lifecycle.coroutines.IListAdapter
import com.lifecycle.coroutines.adapter.RecyclerAdapter
import com.lifecycle.coroutines.util.launchIo
import com.lifecycle.coroutines.util.launchMain
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, ListModel<E, Any, Job> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override val error = MutableLiveData<Throwable>()
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    var httpData: (Int, Int) -> List<E> = { _, _ -> ArrayList() }

    @ExperimentalCoroutinesApi
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) { doGetData(it) }
        loadingState.value = stateStart(AdapterType.refresh)
    }

    @ExperimentalCoroutinesApi
    open fun doGetData(it: Int) {
        if (it != 0 && !isStateRunning(it)) {
            onSubscribe(launchMain {
                flow { emit(httpData(getStartOffset(it), it)) }
                    .flowOn(Dispatchers.IO)
                    .onCompletion { onComplete() }
                    .catch{ onError(it) }
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
