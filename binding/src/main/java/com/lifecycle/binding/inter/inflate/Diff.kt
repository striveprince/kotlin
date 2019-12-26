package com.lifecycle.binding.inter.inflate

interface Diff:Inflate {
    fun key():Int = 0
    fun value():Int = hashCode()
}