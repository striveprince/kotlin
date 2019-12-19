package com.lifecycle.binding.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.util.findLayoutView

interface Parse<T,B>{
    fun t():T
    fun createView(t:T,context: Context, parent: ViewGroup?=null, attachToParent: Boolean=false): View {
        return LayoutInflater.from(context).inflate(findLayoutView(this.javaClass).layout[0],null,false)
    }
    fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B
}