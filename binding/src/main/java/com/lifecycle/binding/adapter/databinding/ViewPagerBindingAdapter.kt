package com.lifecycle.binding.adapter.databinding


import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager.widget.ViewPager
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrollStateChangedListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrolledListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageSelectedListener

/**
 * Created by arvin on 2018/1/15.
 */
object ViewPagerBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun setCurrentItem(view: ViewPager, position: Int) {
        if (getSelectedTabPosition(view) != position) view.currentItem = position
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getSelectedTabPosition(view: ViewPager): Int {
        return view.currentItem
    }

    @JvmStatic
    @BindingAdapter(value = ["pageScrolled", "pageScrollStateChanged", "pageSelected", "positionAttrChanged"], requireAll = false)
    fun addOnPageChangeListener(
        pager: ViewPager,
        onPageScrolledListener: OnPageScrolledListener?,
        onPageScrollStateChangedListener: OnPageScrollStateChangedListener?,
        onPageSelectedListener: OnPageSelectedListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = if (onPageScrolledListener == null && onPageScrollStateChangedListener == null && onPageSelectedListener == null && positionAttrChanged == null) null
        else object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageScrolledListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageScrollStateChangedListener?.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                positionAttrChanged?.onChange()
                onPageSelectedListener?.onPageSelected(position)
            }
        }
        ListenerUtil.trackListener(pager, newValue, R.id.view_pager_layout)?.let {
            pager.removeOnPageChangeListener(it)
        }
        newValue?.let { pager.addOnPageChangeListener(it) }
    }
}