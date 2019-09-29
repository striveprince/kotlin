package com.binding.model.base.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class DataBindingFragment <T>: Fragment(),CycleContainer<T>{
    override val cycle = lifecycle
    override var dataActivity = activity!!

    fun initView(rootView: View): View {
        return rootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inject(savedInstanceState,container,false)
        return  initView(rootView)
    }
}
