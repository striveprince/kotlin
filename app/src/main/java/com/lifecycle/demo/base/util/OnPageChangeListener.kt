package com.lifecycle.demo.base.util

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

class OnPageChange(private val pageChangeListener: ViewPager.OnPageChangeListener):ViewPager2.OnPageChangeCallback() {
    override fun onPageScrollStateChanged(state: Int) {
        super.onPageScrollStateChanged(state)
        pageChangeListener.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        pageChangeListener.onPageSelected(position)
    }
}