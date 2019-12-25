package com.lifecycle.binding.adapter.widget

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.event.IEvent
import com.lifecycle.binding.adapter.event.ObservableEvent
import com.lifecycle.binding.adapter.inter.IList
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable
import kotlin.collections.ArrayList

abstract class WidgetOpenAdapter<E:Inflate,T> : BaseAdapter(), IList<E,T> {

    private val observableEvent: IEvent<E,T> by lazy { this }
    val events: ArrayList<IEvent<E,T>> = ArrayList()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val e = adapterList[position]
        val view = e.createView(parent.context,parent,convertView)
        e.event(observableEvent)
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