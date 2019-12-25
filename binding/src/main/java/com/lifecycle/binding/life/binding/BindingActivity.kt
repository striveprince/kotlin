package com.lifecycle.binding.life.binding

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lifecycle.binding.inter.bind.Binding
import com.lifecycle.binding.life.BaseActivity

abstract class BindingActivity<Model : ViewModel, B:ViewBinding>: BaseActivity<Model, B>(), Binding<Model, B> {
    lateinit var binding:B
    override fun parse(t: Model, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return super.parse(t, context, parent, attachToParent).apply {
            if(this is ViewDataBinding)lifecycleOwner = this@BindingActivity
            binding = this
        }
    }
}