package com.lifecycle.binding.inter.inflate

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.util.findLayoutView

open class ViewBindInflate : BindingInflate<ViewDataBinding> {
    var layoutIndex:Int = 0

    override fun layoutId() = findLayoutView(javaClass).layout[layoutIndex]

}