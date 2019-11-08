package com.lifecycle.binding.inflate

import com.lifecycle.binding.inflate.inter.Parse
import kotlinx.serialization.Transient

abstract class ViewParse<Binding> : Parse<Binding>{
    @Transient  var layoutIndex = 0
    override fun layoutIndex()= layoutIndex
}