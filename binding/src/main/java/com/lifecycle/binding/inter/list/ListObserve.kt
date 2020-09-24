package com.lifecycle.binding.inter.list

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.*

interface ListObserve<E, Binding : ViewDataBinding, Job> : ListObtain<E, Job> {
    val loadingState: ObservableInt
    val errorMessage: ObservableField<CharSequence>
    var callback: Observable.OnPropertyChangedCallback?

    fun init() {
        callback = loadingState.observe { if (state.getAndSet(it) != it && isStateStart(it)) getData(it) }
        start(AdapterType.refresh)
    }


    override fun getData(state: Int) {
        loadingState.set(stateRunning(state))
    }

    override fun start(@AdapterEvent state: Int) {
        if (isStateEnd(this.state.get()))loadingState.set(stateStart(state))
    }


    override fun onNext(t: List<E>) {
        setList(getEndOffset(loadingState.get()), t, stateOriginal(loadingState.get()))
        loadingState.set(stateSuccess(loadingState.get()))
    }

    override fun onSubscribe(job: Job) {
        this.job = job
    }

    override fun onError(e: Throwable) {
        errorMessage.set(e.message)
        loadingState.set(stateError(loadingState.get()))
    }

    override fun onComplete() {
        loadingState.set(stateEnd(loadingState.get()))
    }

    fun destroy() {
        callback?.let { loadingState.removeOnPropertyChangedCallback(it) }
    }
}