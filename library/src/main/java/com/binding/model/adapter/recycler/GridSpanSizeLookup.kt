package com.binding.model.adapter.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.binding.model.adapter.GridInflate

import com.binding.model.adapter.IModelAdapter

/**
 * Created by arvin on 2018/1/24.
 */
open class GridSpanSizeLookup<E : GridInflate<in ViewDataBinding>>(private val adapter: IModelAdapter<E>) :
    GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        return adapter.holderList[position].getSpanSize()
    }

}
