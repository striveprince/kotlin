package com.lifecycle.rx.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import com.lifecycle.rx.IListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenAdapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

abstract class FragmentAdapter<E: Item>(fm: FragmentManager,
                                        behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentOpenAdapter<E,Observable<Any>>(fm,behavior), IListAdapter<E> {

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        return Observable.just(false as Any)
    }



}