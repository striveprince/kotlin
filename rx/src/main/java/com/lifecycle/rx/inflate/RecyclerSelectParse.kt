package com.lifecycle.rx.inflate

import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.rx.R
import com.lifecycle.rx.inflate.list.ListViewInflate


class RecyclerSelectParse<E : Select>(
    private val t: ListViewInflate<E>
) : DataBinding<ListViewInflate<E>, LayoutSwipeRecyclerViewBinding> {
    override fun t()=t
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}