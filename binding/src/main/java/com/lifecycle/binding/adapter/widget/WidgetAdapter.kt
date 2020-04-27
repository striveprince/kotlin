package com.lifecycle.binding.adapter.widget

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.lifecycle.binding.R
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
open class WidgetAdapter<E:Inflate> : BaseAdapter(), IListAdapter<E> {
    override val tag: SparseArray<Any> = SparseArray()
    private val observableEvent: IEvent<E> by lazy { this }
    override val events: ArrayList<IEvent<E>> = ArrayList()
    override val adapterList: MutableList<E> = ArrayList<E>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val e = adapterList[position]
        e.event(observableEvent as IEvent<Any>)
        val view = e.createView(parent.context,parent,convertView)
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