package com.lifecycle.binding.adapter.recycler

import androidx.recyclerview.widget.GridLayoutManager
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.SpanLookup

open class ReverseSpanSizeLookup<E : SpanLookup>(val adapter: IListAdapter<E>, val count: Int) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int) = adapter.adapterList[position]
        .getSpanSize()
        .let {
            when {
                it < 1 -> 1
                it > count -> count
                else -> it
            }
        }
}