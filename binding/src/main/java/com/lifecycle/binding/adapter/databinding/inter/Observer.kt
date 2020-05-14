package com.lifecycle.binding.adapter.databinding.inter

interface Observer<T> {
    fun observer(block:(T)->Unit)
    fun set(t:T)
    fun get():T
}