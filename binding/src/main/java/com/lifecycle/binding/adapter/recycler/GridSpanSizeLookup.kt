package com.lifecycle.binding.adapter.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.lifecycle.binding.adapter.GridInflate

import com.lifecycle.binding.adapter.IModelAdapter

/**
 * Created by arvin on 2018/1/24.
 */
open class GridSpanSizeLookup<E : GridInflate<out ViewDataBinding>>(private val adapter: IModelAdapter<E>) :
    GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        if(position == -1) return 1
        return adapter.holderList[position].getSpanSize()
    }

}
