package com.binding.model.adapter.databinding

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsSpinner
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterViewAnimator
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SpinnerAdapter
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import com.binding.model.adapter.IModelAdapter
import com.binding.model.adapter.recycler.RecyclerAdapter

/**
 * Created by arvin on 2018/1/17.
 */

object ViewBindingAdapter {

    @BindingAdapter("android:alpha")
    fun setAlpha(view: View, alpha: Float) {
        if (!(alpha < 0 || alpha > 1)) view.alpha = alpha
//        if (alpha >= 0 && alpha <= 1) view.alpha = alpha
    }

    @BindingAdapter("adapter")
    fun setAdapter(view: View, adapter: IModelAdapter<*>?) {
        if (adapter == null) return
        if (view is RecyclerView && adapter is RecyclerView.Adapter<*>) {
            view.adapter = adapter
        } else if (view is ViewPager && adapter is PagerAdapter) {
            view.adapter = adapter
        } else if (view is AbsListView && adapter is ListAdapter) {
            view.adapter = adapter
        } else if (view is AbsSpinner && adapter is SpinnerAdapter) {
            view.adapter = adapter
        } else if (view is AdapterViewAnimator && adapter is Adapter) {
            view.adapter = adapter
        } else if (view is AdapterView<*> && adapter is Adapter) {
            view.adapter = adapter
        } else if(view is ViewPager2&&adapter is RecyclerAdapter<*>){
            view.adapter = adapter
        }
    }


    @BindingAdapter("params")
    fun setLayoutParams(view: View, params: ViewGroup.LayoutParams?) {
        if (params != null) view.layoutParams = params
    }


}
