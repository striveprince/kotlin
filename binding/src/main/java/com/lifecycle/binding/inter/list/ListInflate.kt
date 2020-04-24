package com.lifecycle.binding.inter.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.util.*
import com.lifecycle.binding.viewmodel.Obtain
import java.util.concurrent.atomic.AtomicBoolean

interface ListInflate<E, Binding : ViewDataBinding, Job> : ListObserve<E,Binding, Job> , BindingInflate<Binding> {
    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        return super.createView(context, parent, convertView).apply { init() }
    }

}
