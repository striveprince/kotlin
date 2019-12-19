package com.lifecycle.demo.base.util.inflate

import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.viewmodel.RecyclerModel
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.bind.BindParse
import com.lifecycle.binding.inter.inflate.DiffInflate

class RecyclerParse<E : DiffInflate>(
    private val t:RecyclerModel<E>
) : BindParse<RecyclerModel<E>,LayoutSwipeRecyclerViewBinding> {

    override fun t()=t
    override fun layoutId() = R.layout.layout_swipe_recycler_view
}