package com.lifecycle.binding.adapter.databinding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.IEventAdapter
import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.binding.inflate.inter.Measure


/**
 * Company: 中科同创
 * Description:
 * Author: created by Arvin on 2019/9/29 17:09
 * Email: wkz0917@163.com
 * Phone: 15390395799
 */
object ViewGroupBindingAdapter {
    @JvmStatic
    @BindingAdapter("rmInflate")
    fun <E:Inflate<*>>removeInflate(viewGroup: ViewGroup,e:E){
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            val obj = view.getTag(R.id.inflate)
            if (e == obj) {
                viewGroup.removeView(view)
                break
            }
        }
    }

    @JvmStatic
    @BindingAdapter("params")
    fun <E:Measure> setParams(view:View,e:E){
        view.layoutParams = e.measure(view, view.parent as? ViewGroup)
    }

    @JvmStatic
    @BindingAdapter(value = ["addInflate","eventAdapter"])
    fun <E : Inflate<*>> addInflate(viewGroup: ViewGroup,e :E,eventAdapter: IEventAdapter<out Any>?){
//        eventAdapter?.let { e.setEventAdapter(it) }
        val view = e.attachView(viewGroup.context, viewGroup, false, null).root
        view.id = e.getViewId()
        if(e is Measure) viewGroup.addView(view,e.measure(view,viewGroup))
        else viewGroup.addView(view)
        view.setTag(R.id.inflate,e)
    }

    @JvmStatic
    @BindingAdapter(value = ["addInflates","eventAdapter"])
    fun <E : Inflate<*>> addInflates(viewGroup: ViewGroup,es :List<E>,eventAdapter: IEventAdapter<out Any>?){
        viewGroup.removeAllViews()
        for (e in es) {
            addInflate(viewGroup,e,eventAdapter)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["addInflates"])
    fun <E : Inflate<*>> addInflates(viewGroup: ViewGroup,es :List<E>){
        viewGroup.removeAllViews()
        for (e in es) {
            addInflate(viewGroup,e)
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["addInflate"])
    fun <E : Inflate<*>> addInflate(viewGroup: ViewGroup,e :E){
        addInflate(viewGroup,e,null)
    }
}