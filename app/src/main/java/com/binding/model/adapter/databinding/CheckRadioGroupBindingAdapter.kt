package com.binding.model.adapter.databinding

import android.widget.RadioGroup

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil

import com.customers.zktc.R
import com.binding.model.base.view.CheckRadioGroup

/**
 * Created by arvin on 2018/1/15.
 */
@InverseBindingMethods(
    InverseBindingMethod(
        type = CheckRadioGroup::class,
        attribute = "position",
        event = "positionAttrChanged",
        method = "getCheckedPosition"
    )
)
object CheckRadioGroupBindingAdapter {
    @JvmStatic
    @BindingAdapter("position")
    fun checkPosition(radioGroup: CheckRadioGroup, position: Int) {
        if (radioGroup.checkedPosition != position) {
            radioGroup.checkPosition(position)
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["checkedChange", "positionAttrChanged"], requireAll = false)
    fun addOnCheckedChangeListener(
        radioGroup: RadioGroup, listener: RadioGroup.OnCheckedChangeListener?,
        positionAttrChanged: InverseBindingListener?
    ) {
        val newValue = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            listener?.onCheckedChanged(group, checkedId)
            positionAttrChanged?.onChange()
        }
        val oldValue = ListenerUtil.trackListener<RadioGroup.OnCheckedChangeListener>(
            radioGroup,
            newValue,
            R.id.radio_group_layout
        )
        if (oldValue != null) radioGroup.setOnCheckedChangeListener(null)
        radioGroup.setOnCheckedChangeListener(newValue)
    }

}



