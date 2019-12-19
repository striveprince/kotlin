package com.lifecycle.binding.adapter.databinding


import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lifecycle.binding.R


/**
 * description：
 * create developer： admin
 * create time：14:25
 * modify developer：  admin
 * modify time：14:25
 * modify remark：
 *
 * @version 2.0
 */

object SwipeRefreshBindingAdapter {
    @JvmStatic
    @BindingAdapter("refreshing")
    fun setRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
        if (refreshing != isRefreshing(view)) {
            view.isRefreshing = refreshing
        }
    }

    @InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
    @JvmStatic
    fun isRefreshing(view: SwipeRefreshLayout): Boolean {
        return view.isRefreshing
    }

    @JvmStatic
    @BindingAdapter(value = ["onRefreshListener", "refreshingAttrChanged"], requireAll = false)
    fun setOnRefreshListener(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener?, refreshingAttrChanged: InverseBindingListener?) {
        val newValue = if (listener == null && refreshingAttrChanged == null) null else SwipeRefreshLayout.OnRefreshListener {
            refreshingAttrChanged?.onChange()
            listener?.onRefresh()
        }
        ListenerUtil.trackListener<SwipeRefreshLayout.OnRefreshListener>(view, newValue, R.id.swipe_refresh_layout)?.let {
            view.setOnRefreshListener(null)
        }
        view.setOnRefreshListener(newValue)
    }


}
