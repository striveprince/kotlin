package com.lifecycle.rx.inflate

import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.rx.R
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.rx.viewmodel.list.ListViewModel

class RecyclerParse<E : Inflate>(
    private val t: ListViewModel<E>
) : DataBinding<ListViewModel<E>, LayoutSwipeRecyclerViewBinding> {
    override fun t()=t
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}