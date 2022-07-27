package com.lifecycle.rx.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.stateOriginal
import com.lifecycle.binding.util.stateRunning
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {

    override fun getData(state: Int) {
        if (AdapterType.refresh == stateOriginal(state)) {
            loadingState.value = stateRunning(state)
            val es = ArrayList<E>()
            httpData(getStartOffset(state), state)
                .map { if (it is ArrayList) it else ArrayList(it) }
                .doOnSuccess { es.addAll(it) }
                .map { DiffUtil.calculateDiff(DiffUtilCallback(adapterList, it)) }
                .ioToMainThread()
                .doFinally { loadingState.value = AdapterType.no }
                .subscribe(NormalObserver({
                    adapterList.clear()
                    adapterList.addAll(es)
                    it.dispatchUpdatesTo(adapter as ListUpdateCallback)
                }, { onError(it) }, { onComplete() }, { onSubscribe(it) }))
        } else super.getData(state)
    }
}