package com.lifecycle.binding.inflate

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inflate.inter.Inflate

open class ViewInflate<Binding : ViewDataBinding> : ViewParse<Binding>(), Inflate<Binding> {
    lateinit var binding: Binding

    override fun attachView(c: Context, v: ViewGroup?, toParent: Boolean, b: Binding?): Binding {
        binding =  super.attachView(c, v, toParent, b)
        return binding
    }

    override fun binding()=binding
}