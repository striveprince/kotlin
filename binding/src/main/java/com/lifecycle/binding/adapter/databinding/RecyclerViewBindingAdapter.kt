package com.lifecycle.binding.adapter.databinding


import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by arvin on 2018/1/16.
 */


@InverseBindingMethods(
    InverseBindingMethod(
        type = RecyclerView::class,
        attribute = "",
        event = "positionAttrChanged",
        method = ""
    )
)
object RecyclerViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun position(view: RecyclerView, position: Int) {
        view.scrollToPosition(position)
    }

    @BindingAdapter("layout_manager")
    @JvmStatic
    fun setLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager?) {
        if (layoutManager != null) view.layoutManager = layoutManager
    }
    @JvmStatic
    @BindingAdapter("scroll_listener")
    fun setOnScroll(view: RecyclerView, listener: RecyclerView.OnScrollListener?) {
        if (listener != null) view.addOnScrollListener(listener)
    }
}