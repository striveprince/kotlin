package com.lifecycle.binding.inter.anko

import android.content.Context
import android.view.ViewGroup
import com.lifecycle.binding.inter.Parse
import org.jetbrains.anko.AnkoContext

interface AnkoParse<T,B:AnkoContext<Context>> : Parse<T> {
    override fun createView(t:T,context: Context, parent: ViewGroup?, attachToParent: Boolean) =
        parse(t,context).view

    fun parse(t:T,context: Context) : B
}