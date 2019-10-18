package com.binding.model.adapter.recycler

import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.GridInflate
import com.binding.model.adapter.IModelAdapter
import timber.log.Timber

/**
 * Created by arvin on 2018/1/24.
 */

class GridSizeLookup<E : GridInflate<out ViewDataBinding>>(adapter: IModelAdapter<E>, private val size: Int=0) :
    GridSpanSizeLookup<E>(adapter) {

    override fun getSpanSize(position: Int): Int {
        val span = super.getSpanSize(position)
        val count = size / span
        if (size % span != 0) Timber.i("span=%1d,size=%2d,", span, size)
        return if (count == 0) span else count
    }
}
