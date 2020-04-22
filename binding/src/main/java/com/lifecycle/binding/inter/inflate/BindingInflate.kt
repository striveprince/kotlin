@file:Suppress("UNCHECKED_CAST")

package com.lifecycle.binding.inter.inflate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.R
import com.lifecycle.binding.life.AppLifecycle.Companion.appLifecycle

interface BindingInflate<Binding : ViewDataBinding> : Inflate, Recycler {
    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        val binding = parse(convertView, context, parent)
        val view = binding.root
        view.setTag(R.id.dataBinding, binding)
        view.setTag(R.id.inflate, this)
        return view
    }

    fun initBinding(t: Binding) {}


    fun parse(convertView: View?, context: Context, parent: ViewGroup?): Binding {
        return convertView?.convertBinding() ?: binding(context, parent)
            .also { initBinding(it) }
    }


    fun View.convertBinding(): Binding? {
        return getTag(R.id.dataBinding)?.let {
            if (it is ViewDataBinding && layoutIdFromTag() == layoutId()) {
                (it as Binding)
                    .apply { setProperties() }
                    .apply { executePendingBindings() }
            } else null
        }
    }

    fun View.layoutIdFromTag() = getTag(R.id.inflate)?.let { (it as Inflate).layoutId() }


    fun binding(context: Context, parent: ViewGroup?): Binding {
        return (DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, false) as Binding)
            .apply { setProperties() }
    }

    fun Binding.setProperties() {
        setVariable(appLifecycle.parse, this)
        setVariable(appLifecycle.vm, this)
    }
}