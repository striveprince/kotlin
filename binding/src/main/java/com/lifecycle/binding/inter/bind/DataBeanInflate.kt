package com.lifecycle.binding.inter.bind

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.bind.data.DataBindInflate

class DataBeanInflate<Bean,Binding:ViewDataBinding>(private val bean:Bean) : DataBindInflate<Bean, Binding> {
    override fun t(): Bean = bean
    lateinit var holder: RecyclerView.ViewHolder
    override fun attach(t: RecyclerView.ViewHolder) {
        super.attach(t)
        this.holder = t
    }
}