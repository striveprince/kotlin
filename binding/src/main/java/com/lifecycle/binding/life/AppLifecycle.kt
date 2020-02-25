package com.lifecycle.binding.life

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lifecycle.binding.inter.inflate.Inflate
import java.util.*

@Suppress("UNCHECKED_CAST")
open class AppLifecycle constructor(application: Application,
                                       val parse: Int =1,
                                       val vm: Int =2) : Application.ActivityLifecycleCallbacks, LifecycleListener {

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
        appLifecycle = this
        Companion.application = application
        application.registerActivityLifecycleCallbacks(this)
    }


    fun postInitFinish() {
        initFinish = true
        appInit()
    }

    fun addCreateListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.createBlock = createBlock
    }

    fun addResumeListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.resumeBlock = createBlock
    }

    fun addStartListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.startBlock = createBlock
    }

    fun addPauseListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.pauseBlock = createBlock
    }

    fun addStopListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.stopBlock = createBlock
    }

    fun addDestroyListener(createBlock: (LifecycleInit<*>) -> Unit){
        this.destroyBlock = createBlock
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
