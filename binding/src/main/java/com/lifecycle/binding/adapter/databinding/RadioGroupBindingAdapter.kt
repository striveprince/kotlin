package com.lifecycle.binding.adapter.databinding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.lifecycle.binding.R

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
        radioGroup.apply {
            return if (checkedRadioButtonId == -1) -1 else indexOfChild(findViewById(checkedRadioButtonId))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["checkedChange", "positionAttrChanged"], requireAll = false)
    fun addOnCheckedChangeListener(
        radioGroup: RadioGroup, listener: RadioGroup.OnCheckedChangeListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            listener?.onCheckedChanged(group, checkedId)
            positionAttrChanged?.onChange()
        }
        ListenerUtil.trackListener(radioGroup, newValue, R.id.radio_group_layout)?.let {
            radioGroup.setOnCheckedChangeListener(null)
        }
        radioGroup.setOnCheckedChangeListener(newValue)
    }
}



