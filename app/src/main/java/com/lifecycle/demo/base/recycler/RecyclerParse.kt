package com.lifecycle.demo.base.recycler

import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding

@LayoutView(R.layout.layout_swipe_recycler_view)
open class RecyclerParse<E : Inflate> : DataBinding<ListViewModel<E>, LayoutSwipeRecyclerViewBinding> {
}