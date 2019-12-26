package com.lifecycle.binding.life.anko.recycler.diff

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.anko.AnkoActivity
import com.lifecycle.binding.util.recyclerAnko
import com.lifecycle.binding.viewmodel.list.ListDiffViewModel
import com.lifecycle.binding.viewmodel.list.ListViewModel
import org.jetbrains.anko.AnkoContext


abstract class RecyclerAnkoActivity<E: Diff>: AnkoActivity<ListDiffViewModel<E>>() {

    override fun parse(t: ListDiffViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this,t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListDiffViewModel<E> {
        return ViewModelProviders.of(this)[ListViewModel::class.java] as ListDiffViewModel<E>
    }
}