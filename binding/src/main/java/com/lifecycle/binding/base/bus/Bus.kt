package com.lifecycle.binding.base.bus

interface Bus<R> {
    fun send(any: Any)
    fun receiver(block:(Any)->Unit):R
}

