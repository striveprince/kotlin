package com.lifecycle.binding

import android.app.Activity
import android.app.Application
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.binding.util.busPost
import java.util.*

class App constructor(val application: Application) : Application.ActivityLifecycleCallbacks {
    companion object {
        val stack = Stack<Activity>()
        val toolbarList = arrayListOf<Inflate>()
        private val lifeListener = arrayListOf<(LifecycleInit<*>) -> Unit>()
        internal var init = false
        lateinit var application: Application

        fun activity() = stack.lastElement()
        fun getScreenWidth() = application.resources.displayMetrics.widthPixels
        fun getScreenHeight() = application.resources.displayMetrics.heightPixels
        fun dpRcet(left: Int, top: Int, right: Int, bottom: Int) = Rect(left, top, right, bottom)
        fun floatToPx(dp: Float) = application.resources.displayMetrics.density * dp
        fun floatToDp(px: Float) = px / application.resources.displayMetrics.density
        fun toPx(dp: Int): Int = (floatToPx(dp.toFloat()) + 0.5).toInt()
        fun toDp(px: Int) = (floatToDp(px.toFloat()) + 0.5).toInt()
        fun getWeightWidth(sum: Int) = getScreenWidth() / sum
        fun getWeightHeight(sum: Int) = getScreenHeight() / sum
        fun getColor(res: Int) = ContextCompat.getColor(application, res)
        fun colorState(id: Int): ColorStateList = application.resources.getColorStateList(id)
        fun string(strRes: Int, vararg params: Any) = application.getString(strRes, *params)
        fun getDrawable(drawableRes: Int): Drawable? = ContextCompat.getDrawable(application, drawableRes)


        fun startCreate(lifecycleInit: LifecycleInit<*>) {
            for (listener in lifeListener) {
                listener(lifecycleInit)
            }
        }

        fun addLifeInit(it: (LifecycleInit<*>) -> Unit) {
            lifeListener.add(it)
        }
    }

    fun addLifeInit(it: (LifecycleInit<*>) -> Unit) {
        lifeListener.add(it)
    }

    init {
        App.application = application
        application.registerActivityLifecycleCallbacks(this)
        init = true
        busPost(init)
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
