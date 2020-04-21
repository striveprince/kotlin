package com.lifecycle.binding.inter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.R
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.findLayoutView

interface NormalInflate : Inflate {
    fun layoutIndex() = 0

    override fun layoutId(): Int = findLayoutView(javaClass).layout[layoutIndex()]

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        val layoutId = convertView?.getTag(R.id.inflate)?.let { (it as Inflate).layoutId() }
        val view = if (layoutId == layoutId()) convertView
        else LayoutInflater.from(context).inflate(layoutId(), parent, false)
        return view.apply {
            view.setTag(R.id.inflate, this)
            binding(this)
        }
    }

    fun binding(view: View) {}
}