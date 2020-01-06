package com.lifecycle.rx.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.lifecycle.rx.IListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

open class FragmentPager2Adapter<E : Item>(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentOpenPager2Adapter<E, Observable<Any>>(fm, lifecycle), IListAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        return Observable.just(false as Any)
    }
}