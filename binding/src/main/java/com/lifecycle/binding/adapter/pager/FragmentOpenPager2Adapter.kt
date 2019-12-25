package com.lifecycle.binding.adapter.pager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lifecycle.binding.adapter.inter.IList
import com.lifecycle.binding.adapter.inter.IListAdapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

abstract class FragmentOpenPager2Adapter<E: Item,R>(private val fm: FragmentManager, lifecycle:Lifecycle, private val bundle: Bundle = Bundle()):
    FragmentStateAdapter(fm,lifecycle), IList<E,R> {

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

    override fun getItemCount()=size()

    override fun createFragment(position: Int)= adapterList[position].fragment(fm,bundle)
}