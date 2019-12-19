package com.lifecycle.binding.inter.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.Constant
import com.lifecycle.binding.R
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.util.findLayoutView

interface BindParse<T, B : ViewDataBinding> : Parse<T,B> {
    override fun createView(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        val binding  = parse(t, context, parent, attachToParent)
        return binding.root
    }

    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        val binding= DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, attachToParent) as B
        binding.setVariable(Constant.vm,t)
        binding.setVariable(Constant.parse,this)
        return binding
    }

    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]
    fun layoutIndex() = 0
}
