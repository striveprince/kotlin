package com.lifecycle.binding.inter.bind.data

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Recycler
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.util.findLayoutView

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:28
 * Email: 1033144294@qq.com
 */
interface DataBindInflate<T, Binding : ViewDataBinding> : DataBinding<T, Binding>, BindingInflate<Binding>, Recycler {
    val bean:T
    override fun Binding.setProperties() {
        setVariable(AppLifecycle.appLifecycle.parse, this)
        setVariable(AppLifecycle.appLifecycle.vm, bean)
    }

    override fun layoutId() = super<DataBinding>.layoutId()
}