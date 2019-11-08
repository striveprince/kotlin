package com.lifecycle.binding.adapter.databinding


import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager.widget.ViewPager
import com.lifecycle.binding.R

/**
 * Created by arvin on 2018/1/15.
 */
@InverseBindingMethods(
    InverseBindingMethod(
        type = ViewPager::class,
        attribute = "position",
        event = "positionAttrChanged",
        method = "getCurrentItem"
    )
)
object ViewPagerBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun setCurrentItem(view: ViewPager, position: Int) {
        if(view.currentItem!=position)view.currentItem=position
    }

    @JvmStatic
    @BindingAdapter(value = [ "pageChange", "positionAttrChanged" ], requireAll = false)
    fun addOnPageChangeListener(pager: ViewPager, listener: ViewPager.OnPageChangeListener?, positionAttrChanged: InverseBindingListener?) {
        val newValue = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                listener?.onPageScrolled(
                    position,
                    positionOffset,
                    positionOffsetPixels
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
                listener?.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                positionAttrChanged?.onChange()
                listener?.onPageSelected(position)
            }
        }
        val oldValue = ListenerUtil.trackListener(pager, newValue, R.id.view_pager_layout)
        pager.removeOnPageChangeListener(oldValue)
        pager.addOnPageChangeListener(newValue)
    }
}