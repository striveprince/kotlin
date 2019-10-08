package com.binding.model.inflate.inter

import android.view.View
import android.view.ViewGroup

/**
 * Created by pc on 2017/8/29.
 */

interface Measure {
    fun measure(view: View, parent: ViewGroup): ViewGroup.LayoutParams
}
