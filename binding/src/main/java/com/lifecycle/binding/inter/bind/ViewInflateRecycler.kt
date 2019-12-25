package com.lifecycle.binding.inter.bind

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.bind.data.DataBindRecycler

class ViewInflateRecycler<Bean,Binding:ViewDataBinding>(private val bean:Bean) : DataBindRecycler<Bean, Binding> {
    override fun t(): Bean = bean
    lateinit var holder: RecyclerView.ViewHolder
    override fun attach(t: RecyclerView.ViewHolder) {
        super.attach(t)
        this.holder = t
    }


}