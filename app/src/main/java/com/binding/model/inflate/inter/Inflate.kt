package com.binding.model.inflate.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.binding.model.Config
import com.binding.model.adapter.recycler.RecyclerHolder
import timber.log.Timber
import java.lang.RuntimeException

interface Inflate<Binding : ViewDataBinding> : Parse<Binding>{
     override fun attachView(context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Any?): Binding {
        this.binding = bind(getLayoutId(), context, viewGroup, attachToParent, binding1)
        binding?.let { bindView(context,viewGroup, it) }
        return this.binding!!
    }

    fun bindView(context: Context, viewGroup: ViewGroup?, binding: Binding){}

    @Suppress("UNCHECKED_CAST")
    private fun<B:ViewDataBinding> bind(layoutId: Int, context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Any?): B {
        return when (binding1) {
            null -> {
                val b = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, viewGroup, attachToParent) as B
                b.setVariable(Config.vm, this)
                b
            }
            is ViewDataBinding -> {
                binding1.setVariable(Config.vm, this)
                binding1.executePendingBindings()
                binding1 as B
            }
            else -> throw RuntimeException("error bind type = ${binding1.javaClass.name}")
        }
    }

    fun removeBinding(){
    }

    fun getLayoutId(): Int{
        Timber.i("class name=${this.javaClass.name},layoutView.size=${layoutView.layout.size},layoutIndex=${layoutIndex}")
        return layoutView.layout[layoutIndex]
    }

    fun getViewId(): Int = 0

}
