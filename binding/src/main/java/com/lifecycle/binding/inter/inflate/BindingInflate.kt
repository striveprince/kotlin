@file:Suppress("UNCHECKED_CAST")

package com.lifecycle.binding.inter.inflate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    fun initBinding(context: Context,t: Binding) {}

    fun parse(convertView: View?, context: Context, parent: ViewGroup?): Binding {
        return convertView?.convertBinding() ?: binding(context, parent)
            .also {
                if (context is AppCompatActivity) it.lifecycleOwner = context
                initBinding(context,it)
            }
    }


    fun View.convertBinding(): Binding? {
        return getTag(R.id.dataBinding)?.let { it ->
            if (it is ViewDataBinding && layoutIdFromTag() == layoutId()) {
                (it as Binding)
                    .also { setProperties(it) }
                    .apply { executePendingBindings() }
            } else null
        }
    }

    fun View.layoutIdFromTag() = getTag(R.id.inflate)?.let { (it as Inflate).layoutId() }


    fun binding(context: Context, parent: ViewGroup?): Binding {
        return (DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), parent, false) as Binding)
            .also { setProperties(it) }
    }

    fun setProperties(binding: Binding) {
        binding.setVariable(appLifecycle.parse, this)
        binding.setVariable(appLifecycle.vm, this)
    }
}