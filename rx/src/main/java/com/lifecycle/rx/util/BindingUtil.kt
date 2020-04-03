package com.lifecycle.rx.util

import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.rx.inflate.RecyclerParse
import com.lifecycle.rx.inflate.list.ListViewInflate
import com.lifecycle.rx.viewmodel.list.ListViewModel


fun <E : Diff> recyclerBinding(
    lifecycleInit: LifecycleInit<*>,
    t: ListViewModel<E> = ListViewModel()
): LayoutSwipeRecyclerViewBinding {
    return RecyclerParse(t)
        .parse(t, lifecycleInit.requireActivity(), null, false)
}