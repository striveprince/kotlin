package com.lifecycle.binding.inter

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner

interface Init {
    fun initData(owner: LifecycleOwner, bundle: Bundle?=null)
}