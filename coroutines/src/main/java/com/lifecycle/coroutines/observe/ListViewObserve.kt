package com.lifecycle.coroutines.observe

import android.util.SparseArray
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.AdapterType.no
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
import java.util.concurrent.atomic.AtomicInteger

class ListViewObserve<E : Inflate, Binding : ViewDataBinding>(override val adapter: IListAdapter<E> = RecyclerAdapter()) :
    ListObserve<E, Binding, Job> {
    override val array:SparseArray<Any> = SparseArray()
    override var pageWay = true
    override var pageCount = AppLifecycle.pageCount
    override var headIndex = 0
    override var offset = 0
    override val adapterList: MutableList<E> = adapter.adapterList
    override val events: ArrayList<IEvent<E>> = adapter.events
    override val loadingState: ObservableInt = ObservableInt(no)
    lateinit var binding: Binding
    override val errorMessage: ObservableField<CharSequence> = ObservableField()
    var httpData: suspend (Int, Int) -> Flow<List<E>> = { _, _ -> flow { emit(ArrayList<E>()) } }
    override var callback: Observable.OnPropertyChangedCallback? = null
    override var job: Job? = null
    override val state: AtomicInteger = AtomicInteger(no)

    @ExperimentalCoroutinesApi
    override fun getData(state: Int) {
        super.getData(state)
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
    }

    override fun onSubscribe(job: Job) {
        this.job = job
    }

}