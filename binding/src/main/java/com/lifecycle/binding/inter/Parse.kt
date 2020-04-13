package com.lifecycle.binding.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.util.findLayoutView

interface Parse<T,B>{
    fun t():T
    /**
     * 解析layout创建View，这里是用的最简单的解析方式
     * */
    fun createView(t:T,context: Context, parent: ViewGroup?=null, attachToParent: Boolean=false): View {
        return LayoutInflater.from(context).inflate(findLayoutView(this.javaClass).layout[0],parent,attachToParent)
    }
    /**
     * 这里是解析出view的持有类型，如ViewDataBinding
     * */
    fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B
}