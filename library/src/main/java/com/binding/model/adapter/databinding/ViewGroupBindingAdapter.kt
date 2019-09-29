package com.binding.model.adapter.databinding

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.binding.model.R
import com.binding.model.inflate.inter.Inflate



/**
 * Company: 中科同创
 * Description:
 * Author: created by WangKeZe on 2019/9/29 17:09
 * Email: wkz0917@163.com
 * Phone: 15390395799
 */
object ViewGroupBindingAdapter {

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

    @BindingAdapter("addInflate")
    fun <E : Inflate<*>>addInflate(viewGroup: ViewGroup,e :E){
        viewGroup.addView(e.attachView(viewGroup.context,viewGroup,false,null).root)
    }
}