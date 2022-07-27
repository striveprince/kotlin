package com.lifecycle.binding.inter.inflate

interface ErrorInflate :Diff{
    override fun layoutId()=0
    fun set(onLoad: (Int, Int) -> Unit,throwable: Throwable? = null, empty:CharSequence = "")
}