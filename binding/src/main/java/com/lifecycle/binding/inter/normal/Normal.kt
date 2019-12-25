package com.lifecycle.binding.inter.normal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.util.findLayoutView

interface Normal<T> :Parse<T,Any>{
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): Any {
        return ""
    }

    override fun createView(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        return LayoutInflater.from(context).inflate(layoutId(),parent,attachToParent)
    }

    fun layoutId(): Int = findLayoutView(this.javaClass).layout[layoutIndex()]

    fun layoutIndex(): Int = 0
}