package com.lifecycle.binding.adapter

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inflate.inter.Inflate

interface GridInflate<Binding:ViewDataBinding>:Inflate<Binding> {
    fun getSpanSize(): Int
}