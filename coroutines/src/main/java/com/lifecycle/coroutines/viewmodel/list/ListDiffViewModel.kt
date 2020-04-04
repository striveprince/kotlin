package com.lifecycle.coroutines.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.coroutines.util.launchUI
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {

    @ExperimentalCoroutinesApi
    override fun getData(it: Int) {
        if (canRun.getAndSet(false)) {
            onSubscribe(launchUI {
                http.require(getStartOffset(it), it)
                    .map {
                        DiffUtil.calculateDiff(DiffUtilCallback(adapterList, it)).apply {
                            adapterList.clear()
                            adapterList.addAll(it)
                        }
                    }
                    .flowOn(IO)
                    .catch { onError(it) }
                    .onCompletion { onComplete() }
                    .collect { it.dispatchUpdatesTo(adapter as ListUpdateCallback) }
            })
        }
    }
}