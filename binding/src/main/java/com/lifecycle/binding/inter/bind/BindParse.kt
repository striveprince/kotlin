package com.lifecycle.binding.inter.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inter.Parse

interface BindParse<T, B : ViewDataBinding> : Parse<T> {
    override fun createView(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean) =
        parse(t, context, parent, attachToParent).root

    fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B =
        DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, attachToParent)

    fun layoutId(): Int
}
