package com.lifecycle.demo.base.recycler

import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding

open class RecyclerParse<E : Inflate>(private val t: ListViewModel<E>) : DataBinding<ListViewModel<E>, LayoutSwipeRecyclerViewBinding> {
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}