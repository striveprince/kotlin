package com.lifecycle.binding.adapter.databinding

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabItem
import com.lifecycle.binding.R
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.adapter.databinding.inter.OnTabReselectedListener
import com.lifecycle.binding.adapter.databinding.inter.OnTabSelectedListener
import com.lifecycle.binding.adapter.databinding.inter.OnTabUnselectedListener
import com.lifecycle.binding.util.ReflectUtil

/**
 * Created by arvin on 2018/1/17.
 */

object TabLayoutBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun setScrollPosition(layout: TabLayout, position: Int) {
        val current = getSelectedTabPosition(layout)
        if (current == position || position < 0) return
        val tab = layout.getTabAt(position)!!
        ReflectUtil.invoke("selectTab", layout, tab)
        layout.setScrollPosition(position, 0f, true)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getSelectedTabPosition(view: TabLayout): Int {
        return view.selectedTabPosition
    }

    @JvmStatic
    @BindingAdapter(value = ["tabSelected","tabUnselected","tabReselected", "positionAttrChanged"], requireAll = false)
    fun addOnTabSelectedListener(layout: TabLayout,
                                 onTabSelectedListener: OnTabSelectedListener?,
                                 onTabUnselectedListener: OnTabUnselectedListener?,
                                 onTabReselectedListener: OnTabReselectedListener?,
                                 positionAttrChanged: InverseBindingListener?) {
        val newValue =
            if (onTabSelectedListener == null &&onTabUnselectedListener == null &&onTabReselectedListener == null && positionAttrChanged == null) null
             else object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    positionAttrChanged?.onChange()
                    onTabSelectedListener?.onTabSelected(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    onTabUnselectedListener?.onTabUnselected(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    onTabReselectedListener?.onTabReselected(tab)
                }
            }
        ListenerUtil.trackListener(layout, newValue, R.id.tab_layout)?.let {
            layout.removeOnTabSelectedListener(it)
        }
        newValue?.let { layout.addOnTabSelectedListener(it) }
    }


}
