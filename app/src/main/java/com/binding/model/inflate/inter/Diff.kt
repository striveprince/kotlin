package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.recycler.RecyclerHolder

interface Diff<Binding :ViewDataBinding>:Inflate<Binding>{
    fun key(): Int
    fun value(): Int
}