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
    var binding:Binding?
    var iEventAdapter: IEventAdapter<*>?

    fun registerEvent(){
//        RxBus
    }

    fun unRegisterEvent(){

    }

    fun attachView(context: Context, co: ViewGroup?, attachToParent: Boolean, t: Binding?): Binding

//    fun registerEvent() : Boolean{
//        if(isEmpty(layoutView.event)){
//            val events = layoutView.event.split("|")
//            for (event in events) {
//                Config.array.put(event,this)
//            }
//        }
//        return isEmpty(layoutView.event)
//    }
//
//    fun unRegisterEvent(): Boolean{
//        if(isEmpty(layoutView.event)){
//            val events = layoutView.event.split("|")
//            for (event in events) {
//                Config.array.remove(event,this)
//            }
//        }
//        return isEmpty(layoutView.event)
//    }


}