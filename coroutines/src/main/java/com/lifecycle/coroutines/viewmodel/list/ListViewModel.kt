package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.ListModel
import com.lifecycle.coroutines.adapter.RecyclerAdapter
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewModel<E : Inflate>(final override val adapter: IList<E> = RecyclerAdapter()) :
    LifeViewModel(), IList<E>, ListModel<E, Job>, HttpData<List<E>> {
    override var pageWay = true
    override var pageCount = 10
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override val error = MutableLiveData<Throwable>()
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    var http: HttpData<List<E>> = this
    override val canRun: AtomicBoolean = AtomicBoolean(true)
    override suspend fun require(startOffset: Int, it: Int) = flow { emit(ArrayList<E>()) }

    @ExperimentalCoroutinesApi
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) { doGetData(it) }
        loadingState.value = stateStart(AdapterType.refresh)
    }

    @ExperimentalCoroutinesApi
    open fun doGetData(it: Int) {
        if (canStateStart(it) && canRun.getAndSet(false))
            onSubscribe(launchUI {
                http.require(getStartOffset(it), it)
                    .flowOn(Dispatchers.IO)
                    .catch { onError(it) }
                    .onCompletion { onComplete() }
                    .collect { onNext(it) }
            })


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


    override fun onComplete() {
        super.onComplete()
        canRun.compareAndSet(false, true)
    }

}
