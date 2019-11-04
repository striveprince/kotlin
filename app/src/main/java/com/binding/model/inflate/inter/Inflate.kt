package com.binding.model.inflate.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.binding.model.Config

interface Inflate<Binding : ViewDataBinding> : Parse<Binding> {
    override fun attachView(context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Binding?): Binding {
        val binding = bind(getLayoutId(), context, viewGroup, attachToParent, binding1)
        bindView(context, viewGroup, binding)
        return binding
    }

    private fun bind(layoutId: Int, context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Binding?): Binding {
        var b = binding1
        if(binding1 == null)b = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, viewGroup, attachToParent) as Binding
        b!!.setVariable(Config.vm,this)
        if(binding1 != null)b.executePendingBindings()
        return b
    }

    fun bindView(context: Context, viewGroup: ViewGroup?, binding: Binding) {}

    fun removeBinding() {}

    fun getViewId(): Int = 0

    fun attachContainer(context: Context, container: ViewGroup, b: Boolean, any: Any?)=
        @Suppress("UNCHECKED_CAST")
         attachView(context,container,b,any as Binding?)

//    fun getLayoutId(): Int {
////        Timber.i("class name=${this.javaClass.name},layoutView.size=${layoutView.layout.size},layoutIndex=${layoutIndex}")
//        return layoutView.layout[layoutIndex]
//    }

    fun getLayoutId()=layoutView.layout[layoutIndex]
}
