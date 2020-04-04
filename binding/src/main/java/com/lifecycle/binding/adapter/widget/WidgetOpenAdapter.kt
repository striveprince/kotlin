package com.lifecycle.binding.adapter.widget

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.lifecycle.binding.R
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IList
import com.lifecycle.binding.inter.inflate.Inflate
import kotlin.collections.ArrayList

abstract class WidgetOpenAdapter<E:Inflate> : BaseAdapter(), IList<E> {

    private val observableEvent: IEvent<E> by lazy { this }
    val events: ArrayList<IEvent<E>> = ArrayList()
    override val adapterList: MutableList<E> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val e = adapterList[position]
        val view = e.createView(parent.context,parent,convertView)
        e.event(observableEvent as IEvent<Any>)
        view.setTag(R.id.inflate,e)
        return view
    }

    override fun getItem(position: Int)=adapterList[position]

    override fun getItemId(position: Int)=position.toLong()

    override fun getCount() = size()

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

}