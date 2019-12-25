package com.lifecycle.binding.adapter.pager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.lifecycle.binding.adapter.inter.IList
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

abstract class FragmentOpenAdapter<E: Item,R>(private val fm: FragmentManager,
                                              behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                              private val bundle: Bundle = Bundle()):
    FragmentPagerAdapter(fm,behavior), IList<E,R> {
    override val adapterList: MutableList<E> = ArrayList()
    override fun getItem(position: Int) = adapterList[position].fragment(fm,bundle)

    override fun getCount()=size()

    override fun notify(p: Int, type: Int, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun notifyList(p: Int, type: Int, es: List<E>, from: Int): Boolean {
        notifyDataSetChanged()
        return true
    }

//    override fun setEvent(position: Int, e: E, type: Int, view: View?): R {
//        return Observable.just(false as Any)
//    }

    override fun getItemPosition(item: Any): Int {
        if(count>0)return PagerAdapter.POSITION_NONE
        return super.getItemPosition(item)
    }


}