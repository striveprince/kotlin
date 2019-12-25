package com.lifecycle.binding.inter.normal

import android.content.Context
import android.view.ViewGroup
import com.lifecycle.binding.inter.Parse

interface Normal<T> :Parse<T,Any>{
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): Any {
        return ""
    }
}