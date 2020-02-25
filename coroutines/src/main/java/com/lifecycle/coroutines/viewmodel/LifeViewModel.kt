package com.lifecycle.coroutines.viewmodel

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.Init
import kotlinx.coroutines.Job

open class LifeViewModel: ViewModel(),Init{
    internal val jobs = ArrayList<Job>()
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        attachData(owner, bundle)

    }

    open fun attachData(owner: LifecycleOwner, bundle: Bundle?) {}

    override fun onCleared() {
        super.onCleared()
        for (job in jobs) job.cancel()
    }
}