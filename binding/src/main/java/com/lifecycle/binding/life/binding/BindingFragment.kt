package com.lifecycle.binding.life.binding

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lifecycle.binding.inter.bind.Binding
import com.lifecycle.binding.life.BaseFragment

abstract class BindingFragment<Model : ViewModel, B:ViewBinding>: BaseFragment<Model, B>(), Binding<Model, B> {
    lateinit var binding:B
    override fun parse(t: Model, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return super.parse(t, context, parent, attachToParent).apply {
            if(this is ViewDataBinding)lifecycleOwner = this@BindingFragment
            binding = this
        }
    }
}