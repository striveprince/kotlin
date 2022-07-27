package com.lifecycle.binding.adapter.databinding.smartrefresh

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.lifecycle.binding.BuildConfig
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.adapter.databinding.ViewPagerBindingAdapter
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.util.*
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
        if (view.getStateValue() != state) {
            Timber.i("loadingState setState: state=$state condition = ${state.stateCondition()}")
            val s = when {
                isStateStart(state) -> startHttp(state, view)
                isStateEnd(state) -> {
                    when (stateOriginal(state)) {
                        AdapterType.start -> {
                        }
                        AdapterType.refresh -> view.finishRefresh(1000)
                        AdapterType.load -> view.finishLoadMore(1000)
                        else -> {
                            view.finishLoadMore()
                            view.finishRefresh()
                        }
                    }
                    stateEnd(state)
                }
                else -> state
            }
            view.setTag(R.id.smart_refresh_layout_state, s)
        }
    }

    private fun startHttp(state: Int, view: SmartRefreshLayout): Int {
        when (stateOriginal(state)) {
            AdapterType.refresh -> view.autoRefresh()
            AdapterType.load, AdapterType.add -> view.autoLoadMore()
        }
        return stateStart(state)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "state", event = "android:stateAttrChanged")
    fun getState(view: SmartRefreshLayout): Int {
        return view.getStateValue().let {
            if (BuildConfig.DEBUG) Timber.i("getState loadingState = $it condition = ${it.stateCondition()}")
            when {
                view.isRefreshing -> stateStart(AdapterType.refresh)
                view.isLoading -> stateStart(AdapterType.load)
                else -> it
            }
        }
    }

    private fun SmartRefreshLayout.getStateValue(): Int {
        return getTag(R.id.smart_refresh_layout_state) as? Int ?: 0

    }

    @JvmStatic
    @BindingAdapter(value = ["loadMoreListener", "refreshListener", "android:stateAttrChanged"], requireAll = false)
    fun setRefreshingListener(view: SmartRefreshLayout, loadingMode: OnLoadMoreListener?, refreshListener: OnRefreshListener?, stateAttrChanged: InverseBindingListener?) {
        val newValue = if (loadingMode == null && refreshListener == null && stateAttrChanged == null) null
        else object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadingMode?.onLoadMore(refreshLayout)
                stateAttrChanged?.onChange()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshListener?.onRefresh(refreshLayout)
                stateAttrChanged?.onChange()
            }
        }
        ListenerUtil.trackListener(view, newValue, R.id.smart_refresh_layout)?.let { view.setOnRefreshLoadMoreListener(null) }
        newValue?.let { view.setOnRefreshLoadMoreListener(it) }
    }

}

fun SmartRefreshLayout.stateChange(function: (Int) -> Unit) {
    setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            function(stateStart(AdapterType.load))
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            function(stateStart(AdapterType.refresh))
        }
    })
}

fun SmartRefreshLayout.bindState(owner: LifecycleOwner, s: MutableLiveData<Int>) {
    s.observer(owner) { SmartRefreshLayoutBindingAdapter.setState(this, it) }
    stateChange { if (it != s.value) s.value = it }
}

fun SmartRefreshLayout.bindState(s: ObservableInt): Observable.OnPropertyChangedCallback {
    stateChange { if (it != s.get()) s.set(it) }
    return s.observe { SmartRefreshLayoutBindingAdapter.setState(this, it) }
}

fun SmartRefreshLayout.bindState(s: Observer<Int>) {
    s.observer { SmartRefreshLayoutBindingAdapter.setState(this, it) }
    stateChange { if (it != s.get()) s.set(it) }
}