package com.lifecycle.binding.adapter.pager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.lifecycle.binding.IList
import com.lifecycle.binding.inter.inflate.Item

abstract class FragmentOpenAdapter<E: Item>(private val fm: FragmentManager,
                                              behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentPagerAdapter(fm,behavior), IList<E> {
    override val adapterList: MutableList<E> = ArrayList()
    override fun getItem(position: Int) = adapterList[position].fragment(fm)

    override fun getCount()=size()

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

//    override fun setEvent(type: Int, e: E, position: Int, view: View?): R {
//        return Observable.just(false as Any)
//    }

    override fun getItemPosition(item: Any): Int {
        if(count>0)return PagerAdapter.POSITION_NONE
        return super.getItemPosition(item)
    }


}