package com.lifecycle.binding.adapter.databinding


import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.OnScrolledListener
import com.lifecycle.binding.adapter.databinding.inter.StateChangedListener

/**
 * Created by arvin on 2018/1/16.
 */

object RecyclerViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun setPosition(view: RecyclerView, position: Int) {
        if (position != getPosition(view) && position >= 0 && getPosition(view) != -1)
            view.smoothScrollToPosition(position)
    }

    @BindingAdapter("manager")
    @JvmStatic
    fun setLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager?) {
        if (layoutManager != null) view.layoutManager = layoutManager
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getPosition(view: RecyclerView): Int {
        val manager = view.layoutManager
        return if (manager is LinearLayoutManager) manager.findFirstVisibleItemPosition()
        else -1
    }

    @JvmStatic
    @BindingAdapter(value = ["scrollListener","stateChangedListener", "positionAttrChanged"], requireAll = false)
    fun setOnScrollListener(
        view: RecyclerView,
        onScrolledListener: OnScrolledListener?,
        stateChangedListener: StateChangedListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = if (onScrolledListener == null && stateChangedListener == null && positionAttrChanged == null) null
        else object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrolledListener?.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE)//set position when the recyclerView is not scrolling
                    positionAttrChanged?.onChange()
                stateChangedListener?.onScrollStateChanged(recyclerView, newState)
            }
        }
        ListenerUtil.trackListener<RecyclerView.OnScrollListener>(view, newValue, R.id.recycler_view)?.let {
            view.removeOnScrollListener(it)
        }
        newValue?.let { view.addOnScrollListener(it) }
    }
}