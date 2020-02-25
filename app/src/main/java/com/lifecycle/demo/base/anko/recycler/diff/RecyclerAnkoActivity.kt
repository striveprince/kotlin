package com.lifecycle.demo.base.anko.recycler.diff

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.demo.base.anko.AnkoActivity
import com.lifecycle.demo.base.anko.recyclerAnko
import com.lifecycle.rx.viewmodel.list.ListDiffViewModel
import org.jetbrains.anko.AnkoContext


abstract class RecyclerAnkoActivity<E: Diff>: AnkoActivity<ListDiffViewModel<E>>() {

    override fun parse(t: ListDiffViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this, t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProvider(this)[ListDiffViewModel::class.java]  as ListDiffViewModel<E>
    }
}