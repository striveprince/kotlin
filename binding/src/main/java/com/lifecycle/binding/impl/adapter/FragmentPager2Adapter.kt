package com.lifecycle.binding.impl.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.lifecycle.binding.impl.IListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.inter.inflate.Item

open class FragmentPager2Adapter<E : Item>(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentOpenPager2Adapter<E, Any>(fm, lifecycle), IListAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Any {
        return false
    }
}