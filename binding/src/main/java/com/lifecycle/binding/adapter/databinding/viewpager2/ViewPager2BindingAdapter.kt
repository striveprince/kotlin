package com.lifecycle.binding.adapter.databinding.viewpager2

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.ViewPagerBindingAdapter
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrollStateChangedListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrolledListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageSelectedListener
import com.lifecycle.binding.adapter.databinding.positionChange
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

object ViewPager2BindingAdapter {

    @JvmStatic
    @BindingAdapter("position")
    fun setCurrentItem(view: ViewPager2, position: Int) {
        if (getPosition(view) != position) view.currentItem = position
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getPosition(view: ViewPager2): Int {
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
                positionAttrChanged?.onChange()
                onPageSelectedListener?.onPageSelected(position)
            }
        }
        ListenerUtil.trackListener(pager, newValue, R.id.view_pager2_layout)?.let {
            pager.unregisterOnPageChangeCallback(it)
        }
        newValue?.let { pager.registerOnPageChangeCallback(it) }
    }
}


fun ViewPager2.positionChange(function: (Int) -> Unit): ViewPager2.OnPageChangeCallback {
    return object :ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            function(position)
        }
    }
}

fun ViewPager2.bindPosition(owner: LifecycleOwner, s: MutableLiveData<Int>){
    s.observer(owner) { ViewPager2BindingAdapter.setCurrentItem(this,it) }
    positionChange{ s.value = it }
}


fun ViewPager2.bindPosition(s: ObservableInt): Observable.OnPropertyChangedCallback {
    positionChange{ s.set(it) }
    return s.observe { ViewPager2BindingAdapter.setCurrentItem(this,it) }
}


fun ViewPager2.bindPosition(s: Observer<Int>): ViewPager2.OnPageChangeCallback {
    s.observer { ViewPager2BindingAdapter.setCurrentItem(this,it) }
    return positionChange { s.set(it) }
}