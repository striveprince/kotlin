package com.lifecycle.binding.rx.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import com.lifecycle.binding.rx.IRxListAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenAdapter
import com.lifecycle.binding.inter.inflate.Item
import io.reactivex.Observable

abstract class RxFragmentAdapter<E: Item>(fm: FragmentManager,
                                          behavior:Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT):
    FragmentOpenAdapter<E,Observable<Any>>(fm,behavior), IRxListAdapter<E> {

    override fun setEvent(position: Int, e: E, type: Int, view: View?): Observable<Any> {
        return Observable.just(false as Any)
    }



}