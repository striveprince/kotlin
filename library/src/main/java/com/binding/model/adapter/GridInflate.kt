package com.binding.model.adapter

import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.inter.Inflate

interface GridInflate<Binding:ViewDataBinding>:Inflate<Binding> {
    fun getSpanSize(): Int
}