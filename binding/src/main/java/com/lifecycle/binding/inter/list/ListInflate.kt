package com.lifecycle.binding.inter.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inter.inflate.BindingInflate

interface ListInflate<E, Binding : ViewDataBinding, Job> : ListObserve<E,Binding, Job> , BindingInflate<Binding> {
    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply { init() }
    }

}
