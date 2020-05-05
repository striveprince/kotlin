package com.lifecycle.demo.base.recycler.diff

import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.coroutines.viewmodel.list.ListDiffViewModel
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.base.recycler.RecyclerFragment


abstract class RecyclerDiffFragment<E : Diff> : RecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(clazz: Class<ListViewModel<E>>): ListViewModel<E> {
        return ViewModelProvider(this)[ListViewModel::class.java] as ListViewModel<E>
    }
}