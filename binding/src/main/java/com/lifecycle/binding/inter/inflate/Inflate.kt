package com.lifecycle.binding.inter.inflate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.R
import com.lifecycle.binding.util.findLayoutView

interface Inflate {
    fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]

    fun createView(context: Context, parent: ViewGroup? = null, convertView: View? = null): View {
        val layoutId = convertView?.getTag(R.id.inflate)?.let { (it as Inflate).layoutId() }
        val view = if (layoutId == layoutId()) convertView
        else LayoutInflater.from(context).inflate(layoutId(), parent, false)
        return view.also {
            view.setTag(R.id.inflate, this)
            it.binding()
        }
    }

    fun viewId() = 0

    fun layoutIndex() = 0

    fun event(event: IEvent<Any>) {}

    fun View.binding() {}
}