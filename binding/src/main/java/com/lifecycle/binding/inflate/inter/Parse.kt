package com.lifecycle.binding.inflate.inter

import android.content.Context
import android.view.ViewGroup
import com.lifecycle.binding.annoation.LayoutView
import com.lifecycle.binding.findModelView

interface Parse<B>{
    fun layoutView(): LayoutView = findModelView(javaClass)
    fun layoutIndex():Int = 0
    fun attachView(c: Context, v: ViewGroup?, toParent: Boolean, b: B?): B
}