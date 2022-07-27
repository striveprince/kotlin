package com.lifecycle.binding.adapter.recycler

import com.lifecycle.binding.inter.SpanLookup
import com.lifecycle.binding.inter.event.IListAdapter

class OrderSpanSizeLookup<E : SpanLookup>(adapter: IListAdapter<E>, count: Int): ReverseSpanSizeLookup<E>(adapter, count) {

    override fun getSpanSize(position: Int): Int {
        return (count/super.getSpanSize(position))
            .let { if(it<1)1 else it }
    }
}