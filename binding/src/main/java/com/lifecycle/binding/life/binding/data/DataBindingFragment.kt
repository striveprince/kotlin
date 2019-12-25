package com.lifecycle.binding.life.binding.data

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.bind.data.DataBinding
import com.lifecycle.binding.life.binding.BindingFragment

abstract class DataBindingFragment<Model:ViewModel,B:ViewDataBinding>:
    BindingFragment<Model, B>(), DataBinding<Model, B> {

    override fun parse(t: Model, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return super<DataBinding>.parse(t, context, parent, attachToParent).apply {
            lifecycleOwner = this@DataBindingFragment
            binding = this
        }
    }
}