package com.lifecycle.binding.inter.anko

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.inter.Parse

interface AnkoViewParse<T,B:AnkoView> : Parse<T,B> {


    override fun createView(t:T, context: Context, parent: ViewGroup?, attachToParent: Boolean) = parse(t, context).rootView

    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean)=
        parse(t,context)

    fun parse(t:T, context: Context) : B
}