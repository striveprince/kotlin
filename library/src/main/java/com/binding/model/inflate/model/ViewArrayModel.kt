package com.binding.model.inflate.model

import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.IModelAdapter
import com.binding.model.base.container.Container
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.obj.EventType
import com.binding.model.pageWay

open class ViewArrayModel<T : CycleContainer<*>, Binding : ViewDataBinding, E,Adapter:IModelAdapter<E>>
    constructor(val adapter: Adapter)
    :ViewHttpModel<T,Binding,List<E>> ()
{
    override fun onSuccess(t: List<E>) {
        super.onSuccess(t)
        val position = if (pageWay) (offset - headIndex) / pageCount*pageCount else offset
        val headIndex = if (isRefresh()) 0 else this.headIndex
        adapter.setList(position + headIndex, t, EventType.refresh)
    }
}