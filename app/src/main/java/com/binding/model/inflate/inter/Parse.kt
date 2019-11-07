package com.binding.model.inflate.inter

import android.content.Context
import android.text.TextUtils.isEmpty
import android.view.ViewGroup
import com.binding.model.Config
import com.binding.model.adapter.IEventAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.base.RxBus

interface Parse<Binding>{
    val layoutView:LayoutView
    var layoutIndex:Int

    fun <E> setEventAdapter(iEventAdapter: IEventAdapter<E>){
    }

    fun attachView(context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, binding1: Binding?): Binding


}