package com.lifecycle.binding.adapter.recycler

import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.SpanLookup
import timber.log.Timber

class OrderSpanSizeLookup<E : SpanLookup>(adapter: IListAdapter<E>, count: Int): ReverseSpanSizeLookup<E>(adapter, count) {

    override fun getSpanSize(position: Int): Int {
        val span = super.getSpanSize(position)
        val size: Int = count / span
        if (size % span != 0) Timber.i("span=%1d,size=%2d,", span, size)
        return if (size <= 0) 1 else size
    }
}