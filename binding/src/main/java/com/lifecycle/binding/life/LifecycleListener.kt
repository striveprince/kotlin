package com.lifecycle.binding.life

import android.os.Bundle

interface LifecycleListener {
    fun onCreate(lifecycle: LifecycleInit<*>,savedInstanceBundle: Bundle?)
    fun onResume(lifecycle: LifecycleInit<*>)
    fun onStart(lifecycle: LifecycleInit<*>)
    fun onPause(lifecycle: LifecycleInit<*>)
    fun onStop(lifecycle: LifecycleInit<*>)
    fun onDestroy(lifecycle: LifecycleInit<*>)
}