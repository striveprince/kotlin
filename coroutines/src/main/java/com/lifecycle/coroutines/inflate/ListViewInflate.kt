package com.lifecycle.coroutines.inflate

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
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.ListInflate
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.stateStart
import com.lifecycle.coroutines.util.launchUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

open class ListViewInflate<E : Inflate, Binding : ViewDataBinding>(final override val adapter: IListAdapter<E> = RecyclerAdapter()) :
  IListAdapter<E>, ListInflate<E, Binding,Job> {
    override var pageWay = true
    override var pageCount = 10
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
    override fun initBinding(t: Binding) {
        binding = t
    }

    override fun getData(state: Int) {
        if (canRun.getAndSet(false) && isStateStart(state))
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

    override fun onNext(t: List<E>) {
        super.onNext(t)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }
}