package com.lifecycle.binding.inflate.inter

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.adapter.recycler.RecyclerHolder

interface Recycler<Binding :ViewDataBinding>:Inflate<Binding>{
    fun recycler(recyclerHolder: RecyclerHolder<*>)
}