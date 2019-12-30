package com.lifecycle.binding.rx.inflate

import com.lifecycle.binding.R
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.rx.viewmodel.list.RxListViewModel

class RecyclerParse<E : Inflate>(
    private val t: RxListViewModel<E>
) : DataBinding<RxListViewModel<E>, LayoutSwipeRecyclerViewBinding> {
    override fun t()=t
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}