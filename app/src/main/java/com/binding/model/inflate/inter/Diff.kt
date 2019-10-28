package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding

interface Diff<Binding :ViewDataBinding>:Inflate<Binding>{
    fun key(): Int
    fun value(): Int
}