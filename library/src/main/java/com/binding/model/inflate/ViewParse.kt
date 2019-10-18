package com.binding.model.inflate

import com.binding.model.adapter.IEventAdapter
import com.binding.model.findModelView
import com.binding.model.inflate.inter.Parse

abstract class ViewParse<Binding> : Parse<Binding>{
    override var iEventAdapter: IEventAdapter<*>? = null
    override var layoutIndex = 0
    override val layoutView = findModelView(javaClass)
    override var binding: Binding? = null
}