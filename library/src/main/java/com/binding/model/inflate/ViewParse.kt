package com.binding.model.inflate

import com.binding.model.adapter.IEventAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.findModelView
import com.binding.model.inflate.inter.Parse

abstract class ViewParse<Binding> : Parse<Binding>{
    @Transient  override var iEventAdapter: IEventAdapter<*>? = null
    @Transient  override var layoutIndex = 0
    @Transient override var layoutView: LayoutView? = findModelView(javaClass)
    @Transient  override var binding: Binding? = null
}