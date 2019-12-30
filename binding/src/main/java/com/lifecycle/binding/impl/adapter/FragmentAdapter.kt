package com.lifecycle.binding.impl.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import com.lifecycle.binding.impl.IListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenAdapter
import com.lifecycle.binding.inter.inflate.Item

abstract class FragmentAdapter<E: Item>(fm: FragmentManager,
                                        behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentOpenAdapter<E,Any>(fm,behavior), IListAdapter<E> {

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        return false
    }



}