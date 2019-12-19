package com.lifecycle.demo.base.life.binding

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lifecycle.demo.BR
import com.lifecycle.demo.base.life.BaseFragment
import com.lifecycle.binding.inter.bind.BindParse

abstract class DataBindingFragment<Model:ViewModel,Binding:ViewDataBinding>:
    BaseFragment<Model,Binding>(),BindParse<Model,Binding>{
    lateinit var binding: Binding

    override fun parse(t: Model, context: Context, parent: ViewGroup?, attachToParent: Boolean): Binding {
        binding= super.parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding
    }
}