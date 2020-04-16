package com.lifecycle.rx.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

open class FragmentPager2Adapter<E : Item>(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentOpenPager2Adapter<E>(fm, lifecycle), IList<E> {
    override fun setEvent(type: Int, e: E, position: Int, view: View?): Any {
        return Observable.just(false as Any)
    }
}