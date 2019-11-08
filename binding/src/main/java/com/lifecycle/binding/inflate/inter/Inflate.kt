package com.lifecycle.binding.inflate.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.Config

interface Inflate<B : ViewDataBinding> : Parse<B> {
    fun binding(): B
    override fun attachView(c: Context, v: ViewGroup?, toParent: Boolean, b: B?): B {
        val binding = bind(getLayoutId(), c, v, toParent, b)
        bindView(c, v, binding)
        return binding
    }

    private fun bind(layoutId: Int, c: Context, v: ViewGroup?, toParent: Boolean, b: B?): B {
        var binding = b
        if (b == null)
            binding = DataBindingUtil.inflate(LayoutInflater.from(c), layoutId, v, toParent) as B
        binding!!.setVariable(Config.vm, this)
        binding.executePendingBindings()
        return binding
    }

    fun bindView(context: Context, viewGroup: ViewGroup?, binding: B) {}

    fun removeBinding() {}

    fun getViewId(): Int = 0

    @Suppress("UNCHECKED_CAST")
    fun attachContainer(context: Context, container: ViewGroup?, b: Boolean, any: Any?) =
        if (any is ViewDataBinding) attachView(context, container, b, any as B?)
        else attachView(context, container, b,null)
    fun getLayoutId() = layoutView().layout[layoutIndex()]
}
