package com.binding.model.inflate.model

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.binding.model.Config
import com.binding.model.adapter.IEventAdapter
import com.binding.model.base.container.Container
import com.binding.model.base.container.CycleContainer
import com.binding.model.findModelView
import com.binding.model.inflate.ViewInflate
import com.binding.model.inflate.inter.Inflate
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.ListCompositeDisposable
import java.lang.ref.WeakReference

abstract class ViewModel<T: Container,Binding:ViewDataBinding> : ViewInflate<Binding>(), LifecycleObserver {
    private var path: String? = null
    private var bundle: Bundle? = null
    @Transient private var weakReference: WeakReference<T>? = null
    private val disposables = ListCompositeDisposable()
    private lateinit var t:T
    @Suppress("UNCHECKED_CAST")
    fun attachContainer(obj: Any, co: ViewGroup?, attachToParent: Boolean, savedInstanceState: Bundle?): Binding {
        val t = obj as T
        this.t = t
        val binding = attachView(t.dataActivity, co, attachToParent, null)
        weakReference = WeakReference(t)
        if(t is CycleContainer<*>) t.cycle.addObserver(this)
        path = t.dataActivity.intent.getStringExtra(Config.path)
        bundle = t.dataActivity.intent.getBundleExtra(Config.bundle)
        attachView(savedInstanceState, t)
        return binding
    }

    open fun attachView(savedInstanceState: Bundle?, t: T){

    }

    fun finish(){
        t.dataActivity.finish()
    }

    fun onBackPress(){
        t.dataActivity.onBackPressed()
    }

    fun addDisposables(disposable: Disposable){
        disposables.add(disposable)
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        disposables.dispose()
        weakReference?.clear()
        removeBinding()
    }


}