package com.binding.model.adapter.databinding


import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.binding.model.R


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

@InverseBindingMethods(
    InverseBindingMethod(
        type = SwipeRefreshLayout::class,
        attribute = "refreshing",
        event = "refreshingAttrChanged",
        method = "isRefreshing"
    )
)
object SwipeRefreshBindingAdapter {
    @JvmStatic
    @BindingAdapter("refreshing")
    fun setRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
        if (refreshing != view.isRefreshing) {
            view.isRefreshing = refreshing
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["onRefreshListener", "refreshingAttrChanged"], requireAll = false)
    fun setOnRefreshListener(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener?, refreshingAttrChanged: InverseBindingListener?) {
        val newValue = SwipeRefreshLayout.OnRefreshListener {
            refreshingAttrChanged?.onChange()
            listener?.onRefresh()
        }
        val oldValue = ListenerUtil.trackListener<SwipeRefreshLayout.OnRefreshListener>(
            view,
            newValue,
            R.id.swipe_refresh_layout
        )
        if (oldValue != null) view.setOnRefreshListener(null)
        view.setOnRefreshListener(newValue)
    }


}
