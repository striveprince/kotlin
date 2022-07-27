package com.lifecycle.binding.life

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.server.LocalServer
import com.lifecycle.binding.util.contain
import com.lifecycle.binding.view.SwipeBackLayout.Companion.FROM_NO
import java.util.*
import kotlin.collections.ArrayList

typealias LifeListener = ((LifecycleInit<*>) -> Unit)

@Suppress("UNCHECKED_CAST")
open class AppLifecycle constructor(
    application: Application,
    val parse: Int = 1,
    val vm: Int = 2
) : Application.ActivityLifecycleCallbacks, LifecycleListener {
    var createListener: LifeListener = {}
    var startListener: LifeListener = {}
    var resumeListener: LifeListener = {}
    var pauseListener: LifeListener = {}
    var stopListener: LifeListener = {}
    var destroyListener: LifeListener = {}
    var onExitListener: LifeListener = {}
    var onCreateListener: () -> Unit = {}
    private val localServers: ArrayList<LocalServer> = ArrayList()

    companion object {
        var isSwipe: Int = FROM_NO
        lateinit var appLifecycle: AppLifecycle
        val stack = Stack<Activity>()
        val toolbarList = arrayListOf<Inflate>()
        internal var initFinish = false
        lateinit var application: Application
        var pageCount = 10
        var appInit: () -> Unit = {}
        fun activity(): Activity = stack.lastElement()

        fun finishAllWithout(vararg clazz: Class<*>) {
            for (activity in stack) if (!clazz.contain { it.isAssignableFrom(activity.javaClass) }) activity.finish()
        }
    }

    init {
        appLifecycle = this
        Companion.application = application
        application.registerActivityLifecycleCallbacks(this)
    }


    fun postInitFinish() {
        initFinish = true
        appInit()
        onCreateListener()
    }

    fun addLocalServer(localServer: LocalServer) {
        localServers.add(localServer)
    }

    override fun onCreate(lifecycle: LifecycleInit<*>, savedInstanceBundle: Bundle?) {
        createListener(lifecycle)
    }

    override fun onResume(lifecycle: LifecycleInit<*>) {
        resumeListener(lifecycle)
    }

    override fun onStart(lifecycle: LifecycleInit<*>) {
        startListener(lifecycle)
    }

    override fun onPause(lifecycle: LifecycleInit<*>) {
        pauseListener(lifecycle)
    }

    override fun onStop(lifecycle: LifecycleInit<*>) {
        stopListener(lifecycle)
    }

    override fun onDestroy(lifecycle: LifecycleInit<*>) {
        destroyListener(lifecycle)
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
        if (activity is LifecycleInit<*>) {
            if (stack.isEmpty()) {
                localServers.forEach { it.start() }
                onCreateListener()
            }
            onCreate(activity, savedInstanceState)
        }
        stack.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        stack.remove(activity)
        if (activity is LifecycleInit<*>) {
            if (stack.isEmpty()) {
                localServers.forEach { it.stop() }
                onExitListener(activity)
                localServers.clear()
            }
            onDestroy(activity)
        }
    }
}