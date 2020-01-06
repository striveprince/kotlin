package com.lifecycle.rx.viewmodel.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.lifecycle.binding.adapter.recycler.DiffUtilCallback
import com.lifecycle.binding.inter.inflate.Diff
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {
    var d:Disposable? = null
    override fun refreshList(es: List<E>, position: Int): Boolean {
        enable.value = false
        d = Observable.just(DiffUtil.calculateDiff(DiffUtilCallback(adapter.adapterList, es)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.adapterList.clear()
                adapter.adapterList.addAll(es)
                it.dispatchUpdatesTo(adapter as ListUpdateCallback)
                enable.value = true
            }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        d?.dispose()
    }
}