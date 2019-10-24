package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.recycler.RecyclerHolder

interface Recycler<Binding :ViewDataBinding>:Inflate<Binding>{
    fun recycler(recyclerHolder: RecyclerHolder<*>)
}