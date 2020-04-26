package com.lifecycle.binding.adapter.pager

import android.util.SparseIntArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.inter.inflate.Item

class FragmentPager2Adapter<E: Item>(private val fm: FragmentManager, lifecycle:Lifecycle):
    FragmentStateAdapter(fm,lifecycle), IListAdapter<E> {
    override val tag: SparseIntArray= SparseIntArray()
    override val adapterList: MutableList<E> = ArrayList()
    override val events: ArrayList<IEvent<E>> = ArrayList()

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun getItemCount()= size()

    override fun createFragment(position: Int):Fragment{
        return adapterList[position].fragment(fm)
    }
}