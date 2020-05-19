package com.lifecycle.binding.adapter.databinding

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.adapter.databinding.inter.OnTabReselectedListener
import com.lifecycle.binding.adapter.databinding.inter.OnTabSelectedListener
import com.lifecycle.binding.adapter.databinding.inter.OnTabUnselectedListener
import com.lifecycle.binding.adapter.databinding.viewpager2.ViewPager2BindingAdapter
import com.lifecycle.binding.adapter.databinding.viewpager2.positionChange
import com.lifecycle.binding.util.invoke
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

/**
 * Created by arvin on 2018/1/17.
 */

object TabLayoutBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun setPosition(layout: TabLayout, position: Int) {
        val current = getSelectedTabPosition(layout)
        if (current == position || position < 0) return
        val tab = layout.getTabAt(position)!!
        invoke("selectTab", layout, tab)
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


fun TabLayout.positionChange(function: (Int) -> Unit): TabLayout.OnTabSelectedListener {
    return object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab) {}
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabSelected(tab: TabLayout.Tab) { function(selectedTabPosition) }
    }.apply { addOnTabSelectedListener(this) }
}

fun TabLayout.bindPosition(owner: LifecycleOwner, s: MutableLiveData<Int>){
    s.observer(owner) { TabLayoutBindingAdapter.setPosition(this,it) }
    positionChange{ if (it !=s.value)s.value = it }
}


fun TabLayout.bindPosition(s: ObservableInt): Observable.OnPropertyChangedCallback {
    positionChange{ if (it !=s.get())s.set(it) }
    return s.observe { TabLayoutBindingAdapter.setPosition(this,it) }
}

fun TabLayout.bindChange(s: Observer<Int>): TabLayout.OnTabSelectedListener  {
    s.observer { TabLayoutBindingAdapter.setPosition(this,it) }
    return positionChange { if (it !=s.get())s.set(it) }
}