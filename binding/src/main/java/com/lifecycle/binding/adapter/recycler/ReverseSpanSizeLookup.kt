package com.lifecycle.binding.adapter.recycler

import androidx.recyclerview.widget.GridLayoutManager
import com.lifecycle.binding.IList
import com.lifecycle.binding.inter.SpanLookup

open class ReverseSpanSizeLookup<E : SpanLookup>(val adapter: IList<E>, val count: Int) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int) = adapter.adapterList[position]
        .getSpanSize()
        .let { if (it <= 0 || it > count) 1 else it }

}