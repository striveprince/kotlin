package com.lifecycle.binding.adapter.databinding

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsSpinner
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterViewAnimator
import android.widget.ListAdapter
import android.widget.SpinnerAdapter
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.lifecycle.binding.IList
import com.lifecycle.binding.adapter.recycler.RecyclerOpenAdapter

/**
 * Created by arvin on 2018/1/17.
 */

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:alpha")
    fun setAlpha(view: View, alpha: Float) {
        if (!(alpha < 0 || alpha > 1)) view.alpha = alpha
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(view: View, adapter: IList<*>?) {
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
        } else if(view is ViewPager2 &&adapter is RecyclerOpenAdapter<*>){
            view.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("params")
    fun setLayoutParams(view: View, params: ViewGroup.LayoutParams?) {
        if (params != null) view.layoutParams = params
    }


}
