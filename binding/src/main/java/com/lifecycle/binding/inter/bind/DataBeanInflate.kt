package com.lifecycle.binding.inter.bind

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.bind.data.DataBindInflate

open class DataBeanInflate<Bean,Binding:ViewDataBinding>(override val bean: Bean) : DataBindInflate<Bean, Binding> {

    lateinit var holder: RecyclerView.ViewHolder
    override fun attach(t: RecyclerView.ViewHolder) {
        super.attach(t)
        this.holder = t
    }
}