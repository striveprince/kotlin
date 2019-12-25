package com.lifecycle.binding.adapter.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager2.widget.ViewPager2
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrollStateChangedListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrolledListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageSelectedListener

object ViewPager2BindingAdapter {

    @JvmStatic
    @BindingAdapter("position")
    fun setCurrentItem(view: ViewPager2, position: Int) {
        if (getSelectedTabPosition(view) != position) view.currentItem = position
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getSelectedTabPosition(view: ViewPager2): Int {
        return view.currentItem
    }

    @JvmStatic
    @BindingAdapter(value = ["pageScrolled", "pageScrollStateChanged", "pageSelected", "positionAttrChanged"], requireAll = false)
    fun addOnPageChangeListener(
        pager: ViewPager2,
        onPageScrolledListener: OnPageScrolledListener?,
        onPageScrollStateChangedListener: OnPageScrollStateChangedListener?,
        onPageSelectedListener: OnPageSelectedListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = if (onPageScrolledListener == null && onPageScrollStateChangedListener == null && onPageSelectedListener == null && positionAttrChanged == null) null
        else object :ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                onPageScrollStateChangedListener?.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                onPageScrolledListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onPageSelectedListener?.onPageSelected(position)
            }
        }
        ListenerUtil.trackListener(pager, newValue, R.id.view_pager2_layout)?.let {
            pager.unregisterOnPageChangeCallback(it)
        }
        newValue?.let { pager.registerOnPageChangeCallback(it) }
    }
}