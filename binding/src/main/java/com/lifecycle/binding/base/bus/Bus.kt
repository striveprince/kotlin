package com.lifecycle.binding.base.bus

interface Bus<E,R> {
    fun send(any: E)
    fun receiver(block:(E)->Unit):R
}

