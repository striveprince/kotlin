package com.lifecycle.demo.base.anko.inter

import android.content.Context
import android.view.ViewGroup
import com.lifecycle.binding.inter.Parse
import org.jetbrains.anko.AnkoContext

interface AnkoParse<T,B:AnkoContext<Context>> : Parse<T,B> {
    override fun createView(t:T,context: Context, parent: ViewGroup?, attachToParent: Boolean) =
        parse(t,context).view

    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean)=
        parse(t,context)

    fun parse(t:T, context: Context) : B

}