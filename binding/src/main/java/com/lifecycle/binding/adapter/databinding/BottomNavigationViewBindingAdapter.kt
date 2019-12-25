package com.lifecycle.binding.adapter.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.OnNavigationItemSelectedListener

object BottomNavigationViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("check")
    fun setCheck(bottomNavigationView: BottomNavigationView, check: Int) {
        if (getCheck(bottomNavigationView) == check)
            bottomNavigationView.selectedItemId = bottomNavigationView.menu.getItem(check).itemId
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "check", event = "checkAttrChanged")
    fun getCheck(bottomNavigationView: BottomNavigationView): Int {
        return bottomNavigationView.getCheck(bottomNavigationView.selectedItemId)
    }

    @JvmStatic
    fun BottomNavigationView.getCheck(int: Int): Int {
        menu.apply {
            for (index in 0..size()) {
                if (getItem(index).itemId == int)
                    return index
            }
        }
        return -1
    }
    
    @JvmStatic
    @BindingAdapter(value = ["navigationItemSelected", "checkAttrChanged"], requireAll = false)
    fun setOnNavigationItemSelectedListener(
        bottomNavigationView: BottomNavigationView,
        listener: OnNavigationItemSelectedListener?,
        checkAttrChanged: InverseBindingListener?
    ) {
        val newValue = if (checkAttrChanged == null && listener == null) null
        else BottomNavigationView.OnNavigationItemSelectedListener {
            checkAttrChanged?.onChange()
            listener?.onNavigationItemSelected(bottomNavigationView.selectedItemId, bottomNavigationView.getCheck(it.itemId))
            true
        }
        ListenerUtil.trackListener(bottomNavigationView, newValue, R.id.bottom_navigation_view)?.let {
            bottomNavigationView.setOnNavigationItemSelectedListener(null)
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(newValue)
    }
}