package com.lifecycle.binding.inter.list

import androidx.lifecycle.MutableLiveData
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.util.stateEnd
import com.lifecycle.binding.util.stateError
import com.lifecycle.binding.util.stateStart
import com.lifecycle.binding.util.stateSuccess

interface ListModel<E,Job>: IListAdapter<E>, ListObtain<E, Job> {
    val loadingState : MutableLiveData<Int>
    val error : MutableLiveData<Throwable>

    override fun onNext(t: List<E>) {
        loadingState.value?.let {
            setList(getEndOffset(it), t, it)
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