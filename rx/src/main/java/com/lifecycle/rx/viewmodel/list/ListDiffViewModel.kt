package com.lifecycle.rx.viewmodel.list

import com.lifecycle.binding.inter.inflate.Diff

open class ListDiffViewModel<E : Diff> : ListViewModel<E>() {
    override fun refreshList(es: List<E>, position: Int): Boolean {
        enable.value = false
//        diff = CoroutineScope(Dispatchers.Default).launch {
//            val result = DiffUtil.calculateDiff(DiffUtilCallback(adapter.adapterList, es))
//            launch(Dispatchers.Main) {
//                adapter.adapterList.clear()
//                adapter.adapterList.addAll(es)
//                result.dispatchUpdatesTo(adapter as ListUpdateCallback)
//                enable.value = true
//            }
//        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
//        diff?.cancel()
    }
}