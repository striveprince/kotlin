package com.lifecycle.demo.base.anko.recycler

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.lifeModel
import com.lifecycle.demo.base.anko.AnkoFragment
import com.lifecycle.demo.base.anko.recyclerAnko
import com.lifecycle.rx.viewmodel.list.ListViewModel
import org.jetbrains.anko.AnkoContext

abstract class RecyclerAnkoFragment<E: Inflate>: AnkoFragment<ListViewModel<E>>() {

    override fun parse(t: ListViewModel<E>, context: Context): AnkoContext<Context> {
        return recyclerAnko(this, t)
    }



    @Suppress("UNCHECKED_CAST")
    override fun initModel(clazz: Class<ListViewModel<E>>): ListViewModel<E> {
        return lifeModel(ListViewModel::class.java) as ListViewModel<E>
    }
}