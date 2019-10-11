package com.binding.model.inflate.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.binding.model.Config

interface Inflate<Binding : ViewDataBinding> : Parse<Binding>{
    override fun attachView(context: Context, co: ViewGroup?, attachToParent: Boolean, binding: Binding?): Binding {
        this.binding = bind(getLayoutId(), context, co, attachToParent, binding)
        bindView(context,binding!!)
        return this.binding!!
    }

    fun bindView(context: Context, binding: Binding){

    }

    private fun <B : ViewDataBinding> bind(layoutId: Int, context: Context, co: ViewGroup?, attachToParent: Boolean, binding1: B?): B {
        var binding = binding1
        if (binding == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, co, attachToParent)
            binding!!.setVariable(Config.vm, this)
        } else {
            binding.setVariable(Config.vm, this)
            binding.executePendingBindings()
        }
        return binding
    }

    fun removeBinding(){
    }

    fun getLayoutId(): Int{
        return layoutView.layout[layoutIndex]
    }

    fun getViewId(): Int = 0

}
