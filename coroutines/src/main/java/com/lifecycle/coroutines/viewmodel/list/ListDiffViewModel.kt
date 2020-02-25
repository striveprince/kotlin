package com.lifecycle.coroutines.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.isStateRunning
import com.lifecycle.coroutines.util.launchIo
import com.lifecycle.coroutines.util.launchMain

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {

    override fun doGetData(it: Int) {
        val state:Int = loadingState.value!!
        if (it != 0 && !isStateRunning(state)) {
            onSubscribe(launchIo {
                try {
                    val list = httpData(getStartOffset(it), it)
                    DiffUtil.calculateDiff(DiffUtilCallback(adapterList,list)).apply {
                        adapterList.clear()
                        adapterList.addAll(list)
                        dispatchUpdatesTo(adapter as ListUpdateCallback)
                    }
                    launchMain { onNext(list) }
                } catch (e: Exception) {
                    launchMain { onError(e) }
                } finally {
                    launchMain { onComplete() }
                }
            })
        }
    }
}