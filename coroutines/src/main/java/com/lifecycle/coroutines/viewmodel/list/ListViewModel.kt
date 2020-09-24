package com.lifecycle.coroutines.viewmodel.list

import android.os.Bundle
import android.util.SparseArray
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.list.ListModel
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.stateRunning
import com.lifecycle.binding.util.stateStart
import com.lifecycle.coroutines.util.HttpData
import com.lifecycle.coroutines.util.launchUI
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

open class ListViewModel<E : Inflate>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    LifeViewModel(), IListAdapter<E>, ListModel<E, Job> {
    override val array: SparseArray<Any> = SparseArray()
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val loadingState = MutableLiveData(AdapterType.no)
    override var job: Job? = null
    override val adapterList: MutableList<E> = adapter.adapterList
    var httpData: HttpData<E> = { _, _: Int -> flow { emit(ArrayList<E>()) } }
    override val errorMessage: MutableLiveData<CharSequence> = MutableLiveData("")
    override val events: ArrayList<IEvent<E>> = adapter.events
    override val state: AtomicInteger = AtomicInteger()

    @ExperimentalCoroutinesApi
    override fun attachData(owner: LifecycleOwner, bundle: Bundle?) {
        loadingState.observer(owner) {
            Timber.i("loadingState = $it state = ${state.get()}")
            if (state.getAndSet(it) != it && isStateStart(it)) getData(it)
        }
    }

    override fun onSubscribe(job: Job) {
        this.job = job
        addJob(job)
    }

    @ExperimentalCoroutinesApi
    override fun getData(state: Int) {
        super.getData(state)
        httpData(state)
    }

    @ExperimentalCoroutinesApi
    fun httpData(state:Int){
        onSubscribe(launchUI {
            httpData(getStartOffset(state), state)
                .flowOn(Dispatchers.IO)
                .catch { onError(it) }
                .onCompletion { onComplete() }
                .collect { onNext(it) }
        })
    }
}
