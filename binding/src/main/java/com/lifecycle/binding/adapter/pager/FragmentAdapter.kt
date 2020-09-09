package com.lifecycle.binding.adapter.pager

import android.util.SparseArray
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.inflate.Item

class FragmentAdapter<E: Item>(private val fm: FragmentManager,
                                        behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentPagerAdapter(fm,behavior), IListAdapter<E> {
    override val array: SparseArray<Any> = SparseArray()
    override val adapterList: MutableList<E> = ArrayList()
    override fun getItem(position: Int) = adapterList[position].fragment(fm)
    override val events: ArrayList<IEvent<E>> = ArrayList()
    override fun getCount()=size()

    override fun getItemPosition(item: Any): Int {
        if(count>0)return PagerAdapter.POSITION_NONE
        return super.getItemPosition(item)
    }


}