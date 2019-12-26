package com.lifecycle.binding.life.binding.data.recycler.diff

import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.binding.data.recycler.RecyclerFragment
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel


abstract class RecyclerFragment<E : Diff> : RecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProviders.of(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}