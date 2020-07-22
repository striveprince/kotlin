package com.lifecycle.binding.adapter.databinding

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.adapter.databinding.inter.OnSelectItemListener
import com.lifecycle.binding.adapter.databinding.viewpager2.ViewPager2BindingAdapter
import com.lifecycle.binding.adapter.databinding.viewpager2.positionChange
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

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
        return bottomNavigationView.getTag(R.id.bottom_navigation_view) as? Int ?: 0
    }

    @JvmStatic
    @BindingAdapter(value = ["onItemSelected", "positionAttrChanged"], requireAll = false)
    fun setOnNavigationItemSelectedListener(
        v: BottomNavigationView,
        onSelectItemListener: OnSelectItemListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = if (positionAttrChanged == null && onSelectItemListener == null) null
        else BottomNavigationView.OnNavigationItemSelectedListener {
            val position = v.getPosition(it.itemId)
            onSelectItemListener?.onItemSelected(it.itemId, position)
            v.setTag(R.id.bottom_navigation_view, position)
            positionAttrChanged?.onChange()
            true
        }
        ListenerUtil.trackListener(v, newValue, R.id.bottom_navigation_view_selected)
            ?.let { v.setOnNavigationItemSelectedListener(null) }
        v.setOnNavigationItemSelectedListener(newValue)
    }
}


private fun BottomNavigationView.getPosition(int: Int): Int {
    menu.apply {
        for (index in 0 until size()) {
            if (getItem(index).itemId == int)
                return index
        }
    }
    return -1
}


fun BottomNavigationView.positionChange(function:(Int)->Unit) {
    setOnNavigationItemSelectedListener {
        val position = getPosition(it.itemId)
        function(position)
        true
    }
}

fun BottomNavigationView.bindPosition(owner: LifecycleOwner, s: MutableLiveData<Int>){
    s.observer(owner) { BottomNavigationViewBindingAdapter.setPosition(this,it) }
    positionChange{ if (it !=s.value)s.value = it }
}


fun BottomNavigationView.bindPosition(s: ObservableInt): Observable.OnPropertyChangedCallback {
    positionChange{ if (it !=s.get())s.set(it) }
    return s.observe { BottomNavigationViewBindingAdapter.setPosition(this,it) }
}


fun BottomNavigationView.bindPosition(s: Observer<Int>) {
    s.observer { BottomNavigationViewBindingAdapter.setPosition(this,it) }
    return positionChange { if (it !=s.get())s.set(it) }
}