package com.lifecycle.binding.util

import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.util.inflate.RecyclerParse
import com.lifecycle.binding.viewmodel.list.ListViewModel


fun <E : DiffInflate> recyclerBinding(
    lifecycleInit: LifecycleInit<*>,
    t: ListViewModel<E> = ListViewModel()
): LayoutSwipeRecyclerViewBinding {
    return RecyclerParse(t)
        .parse(t, lifecycleInit.getActivity()!!, null, false)
}