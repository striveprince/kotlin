package com.binding.model.base.utils

import android.os.Handler
import android.os.Looper

class MainLooper private constructor(looper: Looper) : Handler(looper) {
    companion object {
        val instance = MainLooper(Looper.getMainLooper())

        fun runOnUiThread(runnable: Runnable) {
            if (isUiThread) {
                runnable.run()
            } else {
                instance.post(runnable)
            }
        }

        val isUiThread: Boolean
            get() = Looper.getMainLooper() == Looper.myLooper()
    }
}