package com.binding.model.base.container

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：14:08
 * modify developer：  admin
 * modify time：14:08
 * modify remark：
 *
 * @version 2.0
 */


interface CycleContainer<T> : Container {
    val component: T
    val cycle: Lifecycle
    fun inject(savedInstanceState: Bundle?, parent: ViewGroup?, attachToParent: Boolean): View
    fun finish()
    fun fragmentManager():FragmentManager
}
