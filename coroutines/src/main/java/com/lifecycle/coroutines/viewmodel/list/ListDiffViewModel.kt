package com.lifecycle.coroutines.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.isStateStart
import com.lifecycle.binding.util.stateOriginal
import com.lifecycle.coroutines.util.launchUI
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {


    @ExperimentalCoroutinesApi
    override fun getData(state: Int) {
        if (AdapterType.refresh == stateOriginal(state)) {
            onSubscribe(launchUI {
                httpData(getStartOffset(state), state)
                    .flowOn(IO)
                    .map { diff(it) }
                    .catch { onError(it) }
                    .onCompletion { onComplete() }
                    .collect { it.dispatchUpdatesTo(adapter as ListUpdateCallback) }
            })
        } else super.getData(state)
    }

    private fun diff(it: List<E>) = DiffUtil.calculateDiff(DiffUtilCallback(adapterList, it))
        .apply { adapterList.clear() }
        .apply { adapterList.addAll(it) }
}