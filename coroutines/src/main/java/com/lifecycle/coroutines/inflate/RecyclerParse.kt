package com.lifecycle.coroutines.inflate

import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.coroutines.R
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding

class RecyclerParse<E : Inflate>(private val t: ListViewModel<E>) : DataBinding<ListViewModel<E>, LayoutSwipeRecyclerViewBinding> {
    override fun t()=t
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}