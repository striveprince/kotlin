package com.lifecycle.demo.base.life

import androidx.databinding.ViewDataBinding
import com.lifecycle.demo.R
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.util.findLayoutView

@LayoutView(layout=[
    R.layout.activity_text,
    R.layout.activity_toolbar
])
class ToolbarInflate(private val index:Int=0) :BindingInflate<ViewDataBinding>{
    override fun layoutId(): Int {
        return findLayoutView(javaClass).layout[index]
    }
}