package com.lifecycle.demo.base.recycler.diff

import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.coroutines.viewmodel.list.ListDiffViewModel
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.base.recycler.RecyclerActivity

abstract class RecyclerDiffActivity<E: Diff> : RecyclerActivity<E>(){

    @Suppress("UNCHECKED_CAST")
    override fun initModel(clazz: Class<ListViewModel<E>>): ListViewModel<E> {
        return ViewModelProvider(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}