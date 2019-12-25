package com.lifecycle.binding.adapter.databinding

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.OnReselectedItemListener
import com.lifecycle.binding.adapter.databinding.inter.OnSelectItemListener
import timber.log.Timber

object BottomNavigationViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("position")
    fun setPosition(bottomNavigationView: BottomNavigationView, position: Int) {
        val currentPosition = getBottomPosition(bottomNavigationView)
        if (currentPosition != position)
            bottomNavigationView.selectedItemId = bottomNavigationView.menu.getItem(position).itemId
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getBottomPosition(bottomNavigationView: BottomNavigationView): Int {
        return bottomNavigationView.getTag(R.id.bottom_navigation_view) as? Int ?:0
//        bottomNavigationView.getPosition(bottomNavigationView.selectedItemId)
    }

    private fun BottomNavigationView.getPosition(int: Int): Int {
        menu.apply {
            for (index in 0..size()) {
                if (getItem(index).itemId == int)
                    return index
            }
        }
        return -1
    }


    @JvmStatic
    @BindingAdapter(value = ["onItemSelected", "positionAttrChanged"], requireAll = false)
//    @BindingAdapter(value = ["onItemSelected", "onItemReselected", "positionAttrChanged"], requireAll = false)
    fun setOnNavigationItemSelectedListener(
        bottomNavigationView: BottomNavigationView,
        onSelectItemListener: OnSelectItemListener?,
//        onReselectedItemListener: OnReselectedItemListener?,
        positionAttrChanged: InverseBindingListener?
    ) {

        val newValue = if (positionAttrChanged == null && onSelectItemListener == null) null
        else BottomNavigationView.OnNavigationItemSelectedListener {
            val position = bottomNavigationView.getPosition(it.itemId)
            onSelectItemListener?.onItemSelected(it.itemId, position)
            bottomNavigationView.setTag(R.id.bottom_navigation_view,position)
            positionAttrChanged?.onChange()
            true
        }

        ListenerUtil.trackListener(bottomNavigationView, newValue, R.id.bottom_navigation_view_selected)?.let {
            bottomNavigationView.setOnNavigationItemSelectedListener(null)
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(newValue)
    }
}