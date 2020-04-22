package com.lifecycle.binding.inter.bind.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.Constant
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.inter.bind.Binding
import com.lifecycle.binding.life.AppLifecycle.Companion.appLifecycle
import com.lifecycle.binding.util.findLayoutView

interface DataBinding<T, B : ViewDataBinding> : Binding<T, B> {
    /**
     * 这里又覆盖了Binding的parse方法，以DataBindingUtil.inflate的方式加载
     * */
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        return runCatching {
            (DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, attachToParent) as B)
                .apply {
                    setVariable(appLifecycle.vm, t)
                    setVariable(appLifecycle.parse, this@DataBinding)
                }
        }.getOrElse { super.parse(t, context, parent, attachToParent) }
    }

    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]
    fun layoutIndex() = 0
}
