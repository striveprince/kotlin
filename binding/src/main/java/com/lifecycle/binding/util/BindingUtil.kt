package com.lifecycle.binding.util

import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.rx.inflate.RecyclerParse
import com.lifecycle.binding.rx.viewmodel.list.RxListViewModel


fun <E : Diff> recyclerBinding(
    lifecycleInit: LifecycleInit<*>,
    t: RxListViewModel<E> = RxListViewModel()
): LayoutSwipeRecyclerViewBinding {
    return RecyclerParse(t)
        .parse(t, lifecycleInit.getActivity()!!, null, false)
}