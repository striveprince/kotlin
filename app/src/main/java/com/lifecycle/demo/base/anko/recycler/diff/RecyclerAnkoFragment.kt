package com.lifecycle.demo.base.anko.recycler.diff

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.demo.base.anko.AnkoFragment
import com.lifecycle.demo.base.anko.recyclerAnko
import com.lifecycle.rx.viewmodel.list.ListDiffViewModel
import org.jetbrains.anko.AnkoContext

abstract class RecyclerAnkoFragment<E: Diff>: AnkoFragment<ListDiffViewModel<E>>() {

    override fun parse(t: ListDiffViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this, t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProviders.of(this)[ListDiffViewModel::class.java] as ListDiffViewModel<E>
    }
}