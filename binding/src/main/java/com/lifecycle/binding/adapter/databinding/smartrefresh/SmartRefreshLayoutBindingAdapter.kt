package com.lifecycle.binding.adapter.databinding.smartrefresh

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.lifecycle.binding.BuildConfig
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.util.isStateRunning
import com.lifecycle.binding.util.stateOriginal
import com.lifecycle.binding.util.stateStart
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import timber.log.Timber

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 12:16
 * Email: 1033144294@qq.com
 */

object SmartRefreshLayoutBindingAdapter {
    @JvmStatic
    @BindingAdapter("state")
    fun setState(view: SmartRefreshLayout, state: Int) {
        if(BuildConfig.DEBUG)Timber.i("view.id = ${view.id} getRefreshing(view) = ${getState(view)} setRefreshing(view,refreshing=$state)")
        if (getState(view) != state) {
            view.setTag(R.id.smart_refresh_layout_state, state)
            if (isStateRunning(state)) {
                when(stateOriginal(state)){
                    AdapterType.refresh -> {
                        view.finishLoadMore()
                        view.autoRefresh()
                    }
                    AdapterType.load, AdapterType.add -> {
                        view.finishRefresh()
                        view.autoLoadMore()
                    }
                }
            }else{
                view.finishLoadMore()
                view.finishRefresh()
            }
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "state", event = "android:stateAttrChanged")
    fun getState(view: SmartRefreshLayout): Int {
        if(BuildConfig.DEBUG)Timber.i("view.id = ${view.id}")
        return when {
            view.isRefreshing -> stateStart(AdapterType.refresh)
            view.isLoading -> stateStart(AdapterType.load)
            else -> view.getTag(R.id.smart_refresh_layout_state) as? Int ?: 0
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["loadMoreListener", "refreshListener", "android:stateAttrChanged"], requireAll = false)
    fun setRefreshingListener(view: SmartRefreshLayout, loadingMode: OnLoadMoreListener?, refreshListener: OnRefreshListener?, stateAttrChanged: InverseBindingListener?) {
        val newValue = if (loadingMode == null && refreshListener == null && stateAttrChanged == null) null
        else {
            object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    if(BuildConfig.DEBUG)Timber.i("onLoadMore(view)")
                    loadingMode?.onLoadMore(refreshLayout)
                    stateAttrChanged?.onChange()
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    if(BuildConfig.DEBUG)Timber.i("onRefresh(view)")
                    refreshListener?.onRefresh(refreshLayout)
                    stateAttrChanged?.onChange()
                }
            }
        }
        if(BuildConfig.DEBUG)Timber.i("newValue = $newValue")
        ListenerUtil.trackListener(view, newValue, R.id.smart_refresh_layout)?.let {
            if(BuildConfig.DEBUG) Timber.i("oldValue = $it")
            view.setOnRefreshLoadMoreListener(null)
        }
        newValue?.let { view.setOnRefreshLoadMoreListener(it) }
    }

}