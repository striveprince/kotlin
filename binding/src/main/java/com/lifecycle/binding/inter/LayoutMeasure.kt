package com.lifecycle.binding.inter

import android.view.View
import android.view.ViewGroup
import com.lifecycle.binding.util.layoutParam

interface LayoutMeasure {
    fun layoutMeasure(viewGroup1: View, viewGroup: ViewGroup): ViewGroup.LayoutParams {
        return viewGroup.layoutParam()
    }
}