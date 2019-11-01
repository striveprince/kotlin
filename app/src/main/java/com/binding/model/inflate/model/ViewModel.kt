package com.binding.model.inflate.model

import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.binding.model.Config
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.ViewInflate
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.ListCompositeDisposable

abstract class ViewModel<T : CycleContainer<*>, Binding : ViewDataBinding> : ViewInflate<Binding>(),
    LifecycleObserver {
    private val disposables = ListCompositeDisposable()
    lateinit var t: T
    lateinit var path: String
    lateinit var bundle: Bundle

    @Suppress("UNCHECKED_CAST")
    fun attachContainer(obj: Any, co: ViewGroup?, attachToParent: Boolean, savedInstanceState: Bundle?): Binding {
        val t1 = obj as T
        t = t1
        val binding = attachView(this.t.dataActivity, co, attachToParent, null)
        t.cycle.addObserver(this)
        t.dataActivity.intent.getStringExtra(Config.path).let { path = it ?: "" }
        t.dataActivity.intent.getBundleExtra(Config.bundle).let { bundle = it ?: Bundle() }
        attachView(savedInstanceState, t)
        return binding
    }

    open fun attachView(savedInstanceState: Bundle?, t: T) {

    }

    open fun finish() {
        t.finish()
    }

    open fun onBackPress() {
        t.dataActivity.onBackPressed()
    }

    fun addDisposables(disposable: Disposable) {
        disposables.add(disposable)
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposables.dispose()
        removeBinding()
    }

    fun onSaveInstanceState(outState: Bundle): Bundle {
        return outState
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {

    }
}