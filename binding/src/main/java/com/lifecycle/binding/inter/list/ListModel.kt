package com.lifecycle.binding.inter.list

import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.util.*

interface ListModel<E,Job>:  ListObtain<E, Job> {
    val loadingState : MutableLiveData<Int>
    val error : MutableLiveData<Throwable>

    override fun onNext(t: List<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, stateOriginal(it))
            loadingState.value = stateSuccess(it)
        }
    }

    override fun start(@AdapterEvent state:Int){
        loadingState.value = stateStart(state)
    }

    override fun onError(e: Throwable) {
        error.value = e
        loadingState.value = stateError(loadingState.value!!)
    }

    override fun onComplete() {
        super.onComplete()
        loadingState.value = stateEnd(loadingState.value!!)
    }
}