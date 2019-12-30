package com.lifecycle.binding.adapter.pager

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lifecycle.binding.IList
import com.lifecycle.binding.inter.inflate.Item

abstract class FragmentOpenPager2Adapter<E: Item,R>(private val fm: FragmentManager, lifecycle:Lifecycle):
    FragmentStateAdapter(fm,lifecycle), IList<E, R> {
    override val adapterList: MutableList<E> = ArrayList()
    override fun notify(p: Int, type: Int, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun getItemCount()= size()

    override fun createFragment(position: Int)= adapterList[position].fragment(fm)
}