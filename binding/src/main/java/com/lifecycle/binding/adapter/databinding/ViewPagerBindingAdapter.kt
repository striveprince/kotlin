package com.lifecycle.binding.adapter.databinding


import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrollStateChangedListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageScrolledListener
import com.lifecycle.binding.adapter.databinding.inter.OnPageSelectedListener
import com.lifecycle.binding.adapter.databinding.viewpager2.ViewPager2BindingAdapter
import com.lifecycle.binding.adapter.databinding.viewpager2.positionChange
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

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
//

fun ViewPager.positionChange(function: (Int) -> Unit): ViewPager.OnPageChangeListener {
    return object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageSelected(position: Int) { function(position) }
    }.apply { addOnPageChangeListener(this) }
}

fun ViewPager.bindPosition(owner: LifecycleOwner, s: MutableLiveData<Int>): ViewPager.OnPageChangeListener {
    s.observer(owner) { ViewPagerBindingAdapter.setCurrentItem(this,it) }
    return positionChange{ s.value = it }
}

fun ViewPager.bindPosition(s:ObservableInt): Observable.OnPropertyChangedCallback {
    positionChange{ if (it !=s.get())s.set(it) }
    return s.observe { ViewPagerBindingAdapter.setCurrentItem(this,it) }
}

fun ViewPager.bindPosition(s: Observer<Int>): ViewPager.OnPageChangeListener {
    s.observer { ViewPagerBindingAdapter.setCurrentItem(this,it) }
     return positionChange { if (it !=s.get())s.set(it) }
}