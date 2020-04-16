package com.lifecycle.coroutines.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.pager.FragmentOpenAdapter
import com.lifecycle.binding.inter.inflate.Item

abstract class FragmentAdapter<E: Item>(fm: FragmentManager,
                                        behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentOpenAdapter<E>(fm,behavior), IList<E> {

    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        return false
    }



}