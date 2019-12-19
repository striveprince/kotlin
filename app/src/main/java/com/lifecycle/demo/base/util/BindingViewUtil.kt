package com.lifecycle.demo.base.util

import com.lifecycle.demo.base.life.viewmodel.RecyclerModel
import com.lifecycle.demo.base.util.inflate.RecyclerParse
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.demo.base.life.LifecycleInit
import com.lifecycle.binding.inter.inflate.DiffInflate


fun <E : DiffInflate> recyclerBinding(
    lifecycleInit: LifecycleInit<*, *, *>,
    t: RecyclerModel<E> = RecyclerModel()
): LayoutSwipeRecyclerViewBinding {

    return RecyclerParse(t)
        .parse(t, lifecycleInit.getActivity()!!, null, false)
}
