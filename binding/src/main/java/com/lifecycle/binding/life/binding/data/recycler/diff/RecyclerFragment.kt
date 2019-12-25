package com.lifecycle.binding.life.binding.data.recycler.diff

import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.life.binding.data.recycler.RecyclerFragment
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel


abstract class RecyclerFragment<E : DiffInflate> : RecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProviders.of(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}