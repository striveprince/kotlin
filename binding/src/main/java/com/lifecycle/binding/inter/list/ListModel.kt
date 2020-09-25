package com.lifecycle.binding.inter.list

import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.util.*

interface ListModel<E, Job> : ListObtain<E, Job> {
    val loadingState: MutableLiveData<Int>
    val errorMessage: MutableLiveData<CharSequence>

    override fun onNext(t: List<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, stateOriginal(it))
            loadingState.value = stateSuccess(it)
        }
    }

    override fun getData(state: Int) {}

    override fun start(@AdapterEvent state: Int) {
        if (isStateEnd(this.state.get())) loadingState.value = stateStart(state)
    }

    override fun onError(e: Throwable) {
        errorMessage.value = e.message
        loadingState.value = stateError(loadingState.value!!)
    }

    override fun onComplete() {
        loadingState.value = stateEnd(loadingState.value!!)
    }
}