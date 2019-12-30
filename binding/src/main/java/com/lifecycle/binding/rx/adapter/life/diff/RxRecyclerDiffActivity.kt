package com.lifecycle.binding.rx.adapter.life.diff

import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.rx.adapter.life.RxRecyclerActivity
import com.lifecycle.binding.rx.viewmodel.list.RxListDiffViewModel


abstract class RxRecyclerDiffActivity<E : Diff> : RxRecyclerActivity<E>() {


    @Suppress("UNCHECKED_CAST")
    override fun initModel(): RxListDiffViewModel<E> {
        return ViewModelProviders.of(this)[RxListDiffViewModel::class.java] as RxListDiffViewModel<E>
    }
}