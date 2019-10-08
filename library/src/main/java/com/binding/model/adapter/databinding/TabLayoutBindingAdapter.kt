package com.binding.model.adapter.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil
import com.binding.model.R
import com.binding.model.ReflectUtil
import com.google.android.material.tabs.TabLayout

/**
 * Created by arvin on 2018/1/17.
 */

@InverseBindingMethods(
    InverseBindingMethod(
        type = TabLayout::class,
        attribute = "position",
        event = "positionAttrChanged",
        method = "getSelectedTabPosition"
    )
)
object TabLayoutBindingAdapter {

    @BindingAdapter("position")
    fun setScrollPosition(layout: TabLayout, position: Int) {
        val current = layout.selectedTabPosition
        if (current == position || position < 0) return
        val tab = layout.getTabAt(position)!!
        ReflectUtil.invoke("selectTab", layout, tab)
        layout.setScrollPosition(position, 0f, true)
    }

    @BindingAdapter(value = [ "tab_selected", "positionAttrChanged" ], requireAll = false)
    fun addOnTabSelectedListener(
        layout: TabLayout,
        listener: TabLayout.OnTabSelectedListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (listener != null) listener!!.onTabSelected(tab)
                if (positionAttrChanged != null) positionAttrChanged!!.onChange()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (listener != null) listener!!.onTabUnselected(tab)
            }

           override fun onTabReselected(tab: TabLayout.Tab) {
                if (listener != null) listener!!.onTabReselected(tab)
            }
        }
        val oldValue = ListenerUtil.trackListener(layout, newValue, R.id.tab_layout)
        if (oldValue != null) layout.removeOnTabSelectedListener(oldValue)
        layout.addOnTabSelectedListener(newValue)
    }
}
