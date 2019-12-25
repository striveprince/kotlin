package com.lifecycle.binding.life.anko.recycler

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.anko.AnkoActivity
import com.lifecycle.binding.util.recyclerAnko
import com.lifecycle.binding.viewmodel.list.ListViewModel
import org.jetbrains.anko.AnkoContext


abstract class RecyclerAnkoActivity<E: Inflate>: AnkoActivity<ListViewModel<E>>() {

    override fun parse(t: ListViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this,t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListViewModel<E> {
        return ViewModelProviders.of(this)[ListViewModel::class.java] as ListViewModel<E>
    }
}