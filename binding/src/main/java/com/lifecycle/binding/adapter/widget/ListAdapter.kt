package com.lifecycle.binding.adapter.widget

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.adapter.IRecyclerAdapter
import com.lifecycle.binding.containsList
import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.binding.R

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 10:11
 * Email: 1033144294@qq.com
 */
open class ListAdapter<E:Inflate<out ViewDataBinding>>:BaseAdapter(),IRecyclerAdapter<E> {
    override val holderList=ArrayList<E>()
    private val iEventAdapter =  this

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val e = holderList[position]
        val v =  if(convertView == null){
             e.attachContainer(parent.context,parent,false,null).root
        }else{
            val out = convertView.getTag(R.id.inflate) as E
            out.removeBinding()
            val binding =if(out.getLayoutId()==e.getLayoutId()) out.binding() else null
            e.attachContainer(parent.context,parent,false,binding).root
        }
//        e.setEventAdapter(iEventAdapter)
        v.setTag(R.id.inflate,e)
        return v
    }


    override fun refreshListAdapter(position: Int, es: List<E>): Boolean {
        if (position == holderList.size || holderList.isEmpty()) return addListAdapter(position, es)
        val eList = ArrayList<E>()
        if (containsList(position, holderList)) eList.addAll(0, holderList.subList(0, position))
        eList.addAll(es)
        holderList.clear()
        holderList.addAll(eList)
        notifyDataSetChanged()
        return true
    }


    override fun addListAdapter(position: Int, es: List<E>): Boolean {
        var p = position
        if (!containsList(position, holderList)) {
            holderList.size
            holderList.addAll(es)
        } else holderList.addAll(p, es)
        notifyDataSetChanged()
        return true
    }
    override fun getItem(position: Int): E {
        return holderList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount()=size()
}