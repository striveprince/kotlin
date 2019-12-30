package com.lifecycle.binding.rx.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.lifecycle.binding.rx.IRxListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

open class RxFragmentPager2Adapter<E : Item>(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentOpenPager2Adapter<E, Observable<Any>>(fm, lifecycle), IRxListAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        return Observable.just(false as Any)
    }
}