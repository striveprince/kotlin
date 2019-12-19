package com.lifecycle.binding.inter.bind

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ViewInflateRecycler<Bean,Binding:ViewDataBinding>(private val bean:Bean) :BindRecycler<Bean,Binding>{
    override fun t(): Bean = bean
    lateinit var holder: RecyclerView.ViewHolder
    override fun attach(t: RecyclerView.ViewHolder) {
        super.attach(t)
        this.holder = t
    }


}