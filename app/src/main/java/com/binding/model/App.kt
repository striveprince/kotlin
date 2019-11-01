package com.binding.model

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import androidx.core.content.ContextCompat
import java.util.*

class App constructor(val application: Application) : Application.ActivityLifecycleCallbacks {
    companion object {
        val stack = Stack<Activity>()
        lateinit var application: Application

        fun activity()=stack.lastElement()

        fun getScreenWidth() = application.resources.displayMetrics.widthPixels

        fun getScreenHeight() = application.resources.displayMetrics.heightPixels

        fun dpRcet(left: Int, top: Int, right: Int, bottom: Int) = Rect(left, top, right, bottom)

        fun floatToPx(dp: Float) = application.resources.displayMetrics.density * dp

        fun floatTodp(px: Float) = px / application.resources.displayMetrics.density

        fun toPx(dp: Int): Int = (floatToPx(dp.toFloat()) + 0.5).toInt()

        fun toDp(px: Int) = (floatTodp(px.toFloat()) + 0.5).toInt()

        fun getWeightWidth(sum: Int) = getScreenWidth() / sum

        fun getWeightHeight(sum: Int) = getScreenHeight() / sum
        fun getColor(res: Int): Int {
            return ContextCompat.getColor(application,res)
        }
    }

    init {
        application.registerActivityLifecycleCallbacks(this)
        App.application = application
    }
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityDestroyed(activity: Activity?) { stack.remove(activity) }
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) { stack.add(activity) }
}
