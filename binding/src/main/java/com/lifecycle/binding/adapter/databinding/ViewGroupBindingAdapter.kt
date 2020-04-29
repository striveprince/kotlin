@file:Suppress("UNCHECKED_CAST")

package com.lifecycle.binding.adapter.databinding

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.R
import com.lifecycle.binding.inter.LayoutMeasure
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.layoutParam
import com.lifecycle.binding.inter.Parse as Parse


/**
 * Company:
 * Description:
 * Author: created by Arvin on 2019/9/29 17:09
 */
object ViewGroupBindingAdapter {

    @JvmStatic
    @BindingAdapter("remove")
    fun <E: Inflate> remove(viewGroup: ViewGroup, e:E){
        val count = viewGroup.childCount
        for (i in 0 until count){
            val view = viewGroup.getChildAt(i)
            val obj = view.getTag(R.id.inflate)
            if(obj == e){
                viewGroup.removeView(view)
                break
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["inflate","event"])
    fun <E : Inflate> addInflate(viewGroup: ViewGroup,e :E,iEvent: IEvent<*>) {
        viewGroup.inflate(e,iEvent)
    }


    @JvmStatic
    @BindingAdapter(value = ["inflate"])
    fun <E : Inflate> inflate(viewGroup: ViewGroup,e :E){
        viewGroup.inflate(e)
    }

    fun<E:Inflate> ViewGroup.inflate(e :E,iEvent: IEvent<*>?=null){
        iEvent?.let { e.event(it as IEvent<Any>) }
        val view = e.createView(context, this, null)
        view.id = e.viewId()
        view.setTag(R.id.inflate,e)
        if (e is LayoutMeasure) addView(view,e.layoutMeasure(view,this))
        else addView(view)
    }


    @JvmStatic
    @BindingAdapter(value = ["inflates","event"])
    fun <E : Inflate> addInflates(viewGroup: ViewGroup, es :List<E>, iEvent: IEvent<*>){
        viewGroup.removeAllViews()
        for (e in es) viewGroup.inflate(e,iEvent)
    }

    @JvmStatic
    @BindingAdapter(value = ["inflates"])
    fun <E : Inflate> addInflates(viewGroup: ViewGroup,es :List<E>){
        viewGroup.removeAllViews()
        for (e in es) viewGroup.inflate(e)
    }




    fun<T,B, E: Parse<T,B>> ViewGroup.parse(e :E,t:T):B{
        val b = e.parse(t,context,this@parse,false)
        val view =  e.run { b.root(context) }
        view.id = e.viewId()
        view.setTag(R.id.parse,e)
        addView(view,if (e is LayoutMeasure)e.layoutMeasure(view,this)else layoutParam())
        return b
    }


    @JvmStatic
    @BindingAdapter("remove")
    fun <E: Parse<*,*>> removeParse(viewGroup: ViewGroup, e:E){
        val count = viewGroup.childCount
        for (i in 0 until count){
            val view = viewGroup.getChildAt(i)
            val obj = view.getTag(R.id.parse)
            if(obj == e){
                viewGroup.removeView(view)
                break
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["parse","vm"])
    fun <T,B,E : Parse<T,B>> addParse(viewGroup: ViewGroup,e :E,vm:T) {
        viewGroup.parse(e,vm)
    }


    @JvmStatic
    @BindingAdapter(value = ["parses","vm"])
    fun <T,B,E: Parse<T,B>> addParses(viewGroup: ViewGroup, es :List<E>, vm:T){
        viewGroup.removeAllViews()
        for (e in es) viewGroup.parse(e,vm)
    }

}
