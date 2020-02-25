package com.lifecycle.coroutines.util

import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.coroutines.viewmodel.list.ListViewModel
import com.lifecycle.coroutines.inflate.RecyclerParse


fun <E : Diff> recyclerBinding(
    lifecycleInit: LifecycleInit<*>,
    t: ListViewModel<E> = ListViewModel()
): LayoutSwipeRecyclerViewBinding {
    return RecyclerParse(t)
        .parse(t, lifecycleInit.getActivity()!!, null, false)
}