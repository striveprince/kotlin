package com.lifecycle.binding.adapter.databinding

import android.text.TextWatcher
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableInt
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

/**
 * Created by arvin on 2018/1/15.
 */
object RadioGroupBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun checkPosition(radioGroup: RadioGroup, position: Int) {
        if (getPosition(radioGroup) != position) {
            radioGroup.apply {
                if (position < 0 || position >= childCount) {
                    this.clearCheck()
                } else {
                    val view = getChildAt(position)
                    if (view != null) check(view.id)
                }
            }
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "position", event = "positionAttrChanged")
    fun getPosition(radioGroup: RadioGroup): Int {
        return radioGroup.getTag(R.id.radio_group_id) as? Int ?:0
    }

    private fun RadioGroup.radioGroupPosition():Int {
        return if (checkedRadioButtonId == -1) -1 else indexOfChild(findViewById(checkedRadioButtonId))
    }

    @JvmStatic
    @BindingAdapter(value = ["checkedChange", "positionAttrChanged"], requireAll = false)
    fun addOnCheckedChangeListener(
        radioGroup: RadioGroup, listener: RadioGroup.OnCheckedChangeListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            listener?.onCheckedChanged(group, checkedId)
            group.setTag(R.id.radio_group_id,radioGroup.radioGroupPosition())
            positionAttrChanged?.onChange()
        }
        ListenerUtil.trackListener(radioGroup, newValue, R.id.radio_group_layout)?.let {
            radioGroup.setOnCheckedChangeListener(null)
        }
        radioGroup.setOnCheckedChangeListener(newValue)
    }
}

fun RadioGroup.positionChange(function: (Int) -> Unit){
    setOnCheckedChangeListener { group, checkedId -> function(group.indexOfChild(group.findViewById(checkedId)))  }
}


fun RadioGroup.bindPosition(s: Observer<Int>){
    s.observer { check(getChildAt(it).id) }
    positionChange{ if (it !=s.get())s.set(it) }
}

fun RadioGroup.bindPosition(s: ObservableInt){
    s.observe { check(getChildAt(it).id) }
    positionChange{ if (it !=s.get())s.set(it) }
}

fun RadioGroup.bindPosition(owner: LifecycleOwner, s: MutableLiveData<Int>){
    s.observer(owner) { check(getChildAt(it).id) }
    positionChange{ if (it !=s.value)s.value = it }
}
