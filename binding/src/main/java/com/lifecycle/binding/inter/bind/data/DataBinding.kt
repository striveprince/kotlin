package com.lifecycle.binding.inter.bind.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.Constant
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.inter.bind.Binding
import com.lifecycle.binding.util.findLayoutView

interface DataBinding<T, B : ViewDataBinding> : Binding<T,B> {

    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return (DataBindingUtil.inflate(LayoutInflater.from(context),layoutId(),parent,attachToParent)as B).apply {
            setVariable(Constant.vm,t)
            setVariable(Constant.parse,this@DataBinding)
        }
    }

    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]
    fun layoutIndex() = 0
}
