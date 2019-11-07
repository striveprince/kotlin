package com.binding.model.inflate

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.inter.Inflate

open class ViewInflate<Binding : ViewDataBinding> : ViewParse<Binding>(), Inflate<Binding> {
    lateinit var binding: Binding

    override fun attachView(context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Binding?): Binding {
        binding =  super.attachView(context, viewGroup, attachToParent, binding1)
        return binding
    }

    override fun binding()=binding
}