package com.lifecycle.coroutines.viewmodel

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.Init

open class LifeViewModel: ViewModel(),Init{
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        
    }
}