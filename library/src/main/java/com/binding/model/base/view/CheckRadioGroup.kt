package com.binding.model.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioGroup

/**
 * Created by arvin on 2018/1/15.
 */

class CheckRadioGroup : RadioGroup {

    val checkedPosition: Int
        get() {
            val checkId = checkedRadioButtonId
            return if (checkId == -1) -1 else indexOfChild(findViewById(checkId))
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun checkPosition(checkPosition: Int) {
        if (checkPosition < 0 || checkPosition >= childCount) {
            this.clearCheck()
        } else {
            val view = getChildAt(checkPosition)
            if (view != null) check(view.id)
        }
    }
}
