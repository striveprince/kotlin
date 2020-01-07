package com.lifecycle.rx.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.rx.observer.NormalObserver
import com.lifecycle.rx.util.ioToMainThread
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {

    override fun doGetData(it: Int) {
        val es = ArrayList<E>()
        if (it != 0 && loadingState.value == AdapterType.no) {
            httpData(getStartOffset(), loadingState.value!!)
                .map { if (it is ArrayList) it else ArrayList(it) }
                .doOnSuccess { es.addAll(it) }
                .map { DiffUtil.calculateDiff(DiffUtilCallback(adapter.adapterList, it)) }
                .ioToMainThread()
                .doFinally { loadingState.value = AdapterType.no }
                .subscribe(NormalObserver({
                    adapter.adapterList.clear()
                    adapter.adapterList.addAll(es)
                    it.dispatchUpdatesTo(adapter as ListUpdateCallback)
                }, { onError(it) }, { onComplete() }, { onSubscribe(it) }))
        }
    }
}