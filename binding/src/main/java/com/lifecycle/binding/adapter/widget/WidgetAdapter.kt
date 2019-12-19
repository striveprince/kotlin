package com.lifecycle.binding.adapter.widget

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.IEvent
import com.lifecycle.binding.adapter.IListAdapter
import com.lifecycle.binding.inter.inflate.Inflate
import io.reactivex.Observable

open class WidgetAdapter<E:Inflate> : BaseAdapter(), IListAdapter<E> {
    override val adapterList = ArrayList<E>()

    private val iEvent: IEvent<E> by lazy { this }
    private val events: ArrayList<IEvent<E>> = ArrayList()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val e = adapterList[position]
        val view = e.createView(parent.context,parent,convertView)
        e.event(iEvent)
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

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        for (event in events) return event.setEvent(position, e, type, view)
        return Observable.just(setIEntity(e,position,type,view))
    }
}