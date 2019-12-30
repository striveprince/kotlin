package com.lifecycle.binding.rx.adapter.life.diff

import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.rx.adapter.life.RxRecyclerFragment
import com.lifecycle.binding.rx.viewmodel.list.RxListDiffViewModel


abstract class RxRecyclerDiffFragment<E : Diff> : RxRecyclerFragment<E>() {

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): RxListDiffViewModel<E> {
        return ViewModelProviders.of(this)[RxListDiffViewModel::class.java] as RxListDiffViewModel<E>
    }
}