package com.lifecycle.binding.inter

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface Parse<T> {
    fun createView(t:T,context: Context, parent: ViewGroup?=null, attachToParent: Boolean=false): View
}