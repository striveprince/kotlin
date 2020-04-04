package com.lifecycle.rx.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.util.canStateStart
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {

    override fun getData(it: Int) {
        val es = ArrayList<E>()
        val state:Int = loadingState.value!!
        if (canRun.getAndSet(false)) {
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
        }
    }

}