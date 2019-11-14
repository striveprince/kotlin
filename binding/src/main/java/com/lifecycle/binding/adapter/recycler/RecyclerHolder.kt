package com.lifecycle.binding.adapter.recycler

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.adapter.IEventAdapter
import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.binding.inflate.inter.Recycler
import com.lifecycle.demo.R

class RecyclerHolder<E:Inflate<*>>
private constructor(private val container:ViewGroup, val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private lateinit var e :E

    constructor(container: ViewGroup,e: E) :
            this(container,e.attachView(container.context,container,false, null)){
        this.e = e
    }

    fun executePendingBindings(e: E,iEventAdapter: IEventAdapter<E>){
        e.removeBinding()
        this.e = e
//        this.e.setEventAdapter(iEventAdapter)
        container.setTag(R.id.holder_position,adapterPosition)
        if(e is Recycler<*>)e.recycler(this)
        val view = this.e.attachContainer(container.context,container,false,binding).root
        view.setTag(R.id.inflate,e)
    }
}

