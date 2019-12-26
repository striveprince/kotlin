package com.lifecycle.binding.life.binding.data.recycler.diff

import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.binding.data.recycler.RecyclerActivity
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel
import com.lifecycle.binding.viewmodel.list.ListViewModel


abstract class RecyclerActivity<E : Diff> : RecyclerActivity<E>() {


    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProviders.of(this)[ListViewModel::class.java] as ListDiffViewModel<E>
    }
}