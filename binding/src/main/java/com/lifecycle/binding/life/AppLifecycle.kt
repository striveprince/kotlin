package com.lifecycle.binding.life

import android.app.Activity
import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.busPost
import java.util.*

class AppLifecycle constructor(application: Application, val parse: Int =1, val vm: Int =2) : Application.ActivityLifecycleCallbacks, LifecycleListener {

    private var createBlock: ((LifecycleInit<*>) -> Unit)? = null
    private var startBlock: ((LifecycleInit<*>) -> Unit)? = null
    private var resumeBlock: ((LifecycleInit<*>) -> Unit)? = null
    private var pauseBlock: ((LifecycleInit<*>) -> Unit)? = null
    private var stopBlock: ((LifecycleInit<*>) -> Unit)? = null
    private var destroyBlock: ((LifecycleInit<*>) -> Unit)? = null

    companion object {
        lateinit var appLifecycle: AppLifecycle
        private val stack = Stack<Activity>()
        val toolbarList = arrayListOf<Inflate>()
        internal var initFinish = false
        lateinit var application: Application
        fun activity(): Activity { return stack.lastElement() }
        var appInit : ()->Unit = {}
    }

    init {
        Companion.application = application
        appLifecycle = this
        application.registerActivityLifecycleCallbacks(this)
    }

    fun postInitFinish() {
        initFinish = true
        appInit()
    }

    fun addCreateListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.createBlock = createBlock
        return this
    }

    fun addResumeListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.resumeBlock = createBlock
        return this
    }

    fun addStartListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.startBlock = createBlock
        return this
    }

    fun addPauseListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.pauseBlock = createBlock
        return this
    }

    fun addStopListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.stopBlock = createBlock
        return this
    }

    fun addDestroyListener(createBlock: (LifecycleInit<*>) -> Unit): AppLifecycle {
        this.destroyBlock = createBlock
        return this
    }

    override fun onCreate(lifecycle: LifecycleInit<*>, savedInstanceBundle: Bundle?) {
        createBlock?.invoke(lifecycle)
    }

    override fun onResume(lifecycle: LifecycleInit<*>) {
        resumeBlock?.invoke(lifecycle)
    }

    override fun onStart(lifecycle: LifecycleInit<*>) {
        startBlock?.invoke(lifecycle)
    }

    override fun onPause(lifecycle: LifecycleInit<*>) {
        pauseBlock?.invoke(lifecycle)
    }

    override fun onStop(lifecycle: LifecycleInit<*>) {
        stopBlock?.invoke(lifecycle)
    }

    override fun onDestroy(lifecycle: LifecycleInit<*>) {
        destroyBlock?.invoke(lifecycle)
    }

    override fun onActivityPaused(activity: Activity?) {
        if (activity is LifecycleInit<*>) onPause(activity)
    }

    override fun onActivityResumed(activity: Activity?) {
        if (activity is LifecycleInit<*>) onResume(activity)
    }

    override fun onActivityStarted(activity: Activity?) {
        if (activity is LifecycleInit<*>) onStart(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {
        if (activity is LifecycleInit<*>) onStop(activity)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        stack.add(activity)
        if (activity is LifecycleInit<*>) onCreate(activity, savedInstanceState)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        stack.remove(activity)
        if (activity is LifecycleInit<*>) onDestroy(activity)
    }
}
