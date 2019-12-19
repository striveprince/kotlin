package com.lifecycle.binding.base.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup

/**
 * Created by arvin on 2018/1/15.
 */

class CheckRadioGroup : RadioGroup {

    val checkedPosition = if (checkedRadioButtonId == -1) -1 else indexOfChild(findViewById(checkedRadioButtonId))

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun checkPosition(position: Int) {
        if (position < 0 || position >= childCount) {
            this.clearCheck()
        } else {
            val view = getChildAt(position)
            if (view != null) check(view.id)
        }
    }
}
