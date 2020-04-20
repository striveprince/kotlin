package com.lifecycle.rx.recycler.diff

import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.rx.recycler.RecyclerFragment
import com.lifecycle.rx.viewmodel.list.ListDiffViewModel


abstract class RecyclerDiffFragment<E : Diff> : RecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProvider(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}