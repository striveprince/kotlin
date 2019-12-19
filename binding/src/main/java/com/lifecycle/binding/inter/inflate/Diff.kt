package com.lifecycle.binding.inter.inflate

interface Diff {
    fun key():Int = 0
    fun value():Int = hashCode()

}