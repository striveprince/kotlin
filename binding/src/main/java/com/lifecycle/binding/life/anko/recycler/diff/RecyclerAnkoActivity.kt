package com.lifecycle.binding.life.anko.recycler.diff

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.anko.AnkoActivity
import com.lifecycle.binding.util.recyclerAnko
import com.lifecycle.binding.rx.viewmodel.list.RxListDiffViewModel
import org.jetbrains.anko.AnkoContext


abstract class RecyclerAnkoActivity<E: Diff>: AnkoActivity<RxListDiffViewModel<E>>() {

    override fun parse(t: RxListDiffViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this,t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): RxListDiffViewModel<E> {
        return ViewModelProviders.of(this)[RxListDiffViewModel::class.java] as RxListDiffViewModel<E>
    }
}