package com.lifecycle.binding.life.anko.recycler

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.anko.AnkoFragment
import com.lifecycle.binding.util.recyclerAnko
import com.lifecycle.binding.rx.viewmodel.list.RxListViewModel
import org.jetbrains.anko.AnkoContext

abstract class RecyclerAnkoFragment<E: Inflate>: AnkoFragment<RxListViewModel<E>>() {

    override fun parse(t: RxListViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this,t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): RxListViewModel<E> {
        return ViewModelProviders.of(this)[RxListViewModel::class.java] as RxListViewModel<E>
    }
}