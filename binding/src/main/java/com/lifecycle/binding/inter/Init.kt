package com.lifecycle.binding.inter

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner

interface Init<Api> {
    fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?=null)
}