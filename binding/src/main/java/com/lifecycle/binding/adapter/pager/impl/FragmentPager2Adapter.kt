package com.lifecycle.binding.adapter.pager.impl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.lifecycle.binding.adapter.inter.IListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

open class FragmentPager2Adapter<E : Item>(fm: FragmentManager, lifecycle: Lifecycle, bundle: Bundle = Bundle())
    : FragmentOpenPager2Adapter<E, Observable<Any>>(fm, lifecycle, bundle), IListAdapter<E> {
    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        return Observable.just(false as Any)
    }

}