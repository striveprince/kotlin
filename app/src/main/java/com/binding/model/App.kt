package com.binding.model

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.content.ContextCompat
import java.util.*

class App constructor(val application: Application) : Application.ActivityLifecycleCallbacks {
    companion object {
        val stack = Stack<Activity>()
        lateinit var application:Application
        fun activity():Activity{
            return stack.lastElement()
        }



        fun getScreenWidth(): Int {
            return application.resources.displayMetrics.widthPixels
        }

        fun getScreenHeight(): Int {
            return application.resources.displayMetrics.heightPixels
        }

        fun dipTopx(dp: Float): Float {
            return application.resources.displayMetrics.density * dp
        }

        fun pxTodip(px: Float): Float {
            return px / application.resources.displayMetrics.density
        }

        fun getWeightWidth(sum: Int): Int {
            return getScreenWidth() / sum
        }

        fun getWeightHeight(sum: Int): Int {
            return getScreenHeight() / sum
        }
    }

    init {
        application.registerActivityLifecycleCallbacks(this)
        App.application = application
    }

    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityDestroyed(activity: Activity?) {
        stack.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        stack.add(activity)
    }


}
