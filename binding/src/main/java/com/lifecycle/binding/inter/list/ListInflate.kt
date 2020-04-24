package com.lifecycle.binding.inter.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.Obtain
import java.util.concurrent.atomic.AtomicBoolean

interface ListInflate<E, Binding : ViewDataBinding, Job> : IListAdapter<E>, ListObtain<E, Job> , BindingInflate<Binding> {
    val loadingState: ObservableInt
    val error: ObservableField<Throwable>
    var callback: Observable.OnPropertyChangedCallback?

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply { init() }
    }

    fun init(){
        callback = loadingState.observe {  if (canRun.getAndSet(false) && isStateStart(it))getData(it) }
        start(AdapterType.refresh)
    }

    override fun start(@AdapterEvent state: Int) {
        loadingState.set(stateStart(state))
    }


    override fun onNext(t: List<E>) {
        setList(getEndOffset(loadingState.get()), t, loadingState.get())
        loadingState.set(stateSuccess(loadingState.get()))
    }

    override fun onSubscribe(job: Job) {
        this.job = job
    }

    override fun onError(e: Throwable) {
        error.set(e)
        loadingState.set(stateError(loadingState.get()))
    }

    override fun onComplete() {
        super.onComplete()
        loadingState.set(stateEnd(loadingState.get()))
    }


    fun destroy() {
        callback?.let { loadingState.removeOnPropertyChangedCallback(it) }
    }
}
