package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding

interface Recycler<Binding :ViewDataBinding>:Inflate<Binding>{
    fun key(): Any
    fun areContentsTheSame(parseRecycler: Parse<*>): Boolean
    fun check(check: Boolean)
    fun getCheckType(): Int
    /**
     * 0       1       2       3
     * push     false   true   false    true
     * takeBack false   false  true     true
     */
    fun checkWay(): Int
}