package com.lifecycle.binding.life.liveData

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EventLiveData<T>(t:T? = null) : MutableLiveData<T>(t) {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        LiveEventObserver.bind(this, owner, observer)
    }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

//    override fun setValue(value: T) {
//        if (Thread.currentThread() == Looper.getMainLooper().thread) {
//            super.setValue(value)
//        } else {
//            handler.post { super.setValue(value) }
//        }
//    }

    override fun postValue(d: T) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            setValue(d)
        } else {
            handler.post { setValue(d) }
        }
    }
}