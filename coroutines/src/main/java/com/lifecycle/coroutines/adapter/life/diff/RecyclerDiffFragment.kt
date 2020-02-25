package com.lifecycle.coroutines.adapter.life.diff

import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.coroutines.adapter.life.RecyclerFragment
import com.lifecycle.coroutines.viewmodel.list.ListDiffViewModel


abstract class RecyclerDiffFragment<E : Diff> : RecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProvider(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}