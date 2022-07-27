package com.lifecycle.binding.adapter.pager

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.inflate.Item

class FragmentPager2Adapter<E: Item>(private val fm: FragmentManager, lifecycle:Lifecycle):
    FragmentStateAdapter(fm,lifecycle), IListAdapter<E> {
    override val array: SparseArray<Any> = SparseArray()
    override val adapterList: MutableList<E> = ArrayList()
    override val events: ArrayList<IEvent<E>> = ArrayList()

    override fun getItemCount()= size()

    override fun createFragment(position: Int):Fragment{
        return adapterList[position].fragment(fm)
    }
}