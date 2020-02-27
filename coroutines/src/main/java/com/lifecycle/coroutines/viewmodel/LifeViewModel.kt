package com.lifecycle.coroutines.viewmodel

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.Init
import kotlinx.coroutines.Job

open class LifeViewModel: ViewModel(),Init{
    private val jobs = ArrayList<Job>()
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        attachData(owner, bundle)
    }

    open fun attachData(owner: LifecycleOwner, bundle: Bundle?) {}

    fun addJob(job:Job){
        jobs.add(job)
    }

    override fun onCleared() {
        super.onCleared()
        for (job in jobs) job.cancel()
    }
}