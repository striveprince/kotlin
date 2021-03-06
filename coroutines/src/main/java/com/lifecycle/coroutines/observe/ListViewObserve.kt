package com.lifecycle.coroutines.observe

import android.util.SparseArray
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.list.ListObserve
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.coroutines.util.launchUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

class ListViewObserve<E : Inflate, Binding : ViewDataBinding>(override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    ListObserve<E, Binding, Job> {
    override val array:SparseArray<Any> = SparseArray()
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val adapterList: MutableList<E> = adapter.adapterList
    override val events: ArrayList<IEvent<E>> = adapter.events
    override val loadingState: ObservableInt = ObservableInt(AdapterType.no)
    lateinit var binding: Binding
    override val error: ObservableField<Throwable> = ObservableField()
    override val canRun: AtomicBoolean = AtomicBoolean(true)
    var httpData: suspend (Int, Int) -> Flow<List<E>> = { _, _ -> flow { emit(ArrayList<E>()) } }
    override var callback: Observable.OnPropertyChangedCallback? = null
    override var job: Job? = null

    @ExperimentalCoroutinesApi
    override fun getData(state: Int) {
        onSubscribe(launchUI {
            httpData(getStartOffset(state), state)
                .flowOn(Dispatchers.IO)
                .catch { onError(it) }
                .onCompletion { onComplete() }
                .collect { onNext(it) }
        })
    }


    override fun onComplete() {
        super.onComplete()
        job?.cancel()
        canRun.compareAndSet(false, true)
    }

    override fun onSubscribe(job: Job) {
        this.job = job
    }

}