package com.lifecycle.binding.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.util.findLayoutView

interface Parse<T,B>{

    /**
     * 解析layout创建View，这里是用的最简单的解析方式
     * */
    fun createView(t:T,context: Context, parent: ViewGroup?=null, attachToParent: Boolean=false) =
        parse(t, context, parent, attachToParent).root(context)
            .also { parent?.let { if(this is LayoutMeasure)it.layoutParams = layoutMeasure(it,parent) }}

    fun layoutIndex() = 0

    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]

    /**
     * 这里是解析出view的持有类型，如ViewDataBinding
     * */
    fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B

    fun B.root(context: Context,parent: ViewGroup? = null, attachToParent: Boolean = false):View =
        LayoutInflater.from(context).inflate(layoutId(),parent,attachToParent)

    fun viewId()=0


}