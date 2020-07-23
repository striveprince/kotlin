package com.lifecycle.binding.life.liveData

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.*
import com.lifecycle.binding.util.toJson
import timber.log.Timber


class LiveEventObserver<T>(
    private val liveData: LiveData<T>,
    private val owner: LifecycleOwner,
    private val observer: Observer<in T>,
    private val pending: ArrayList<T> = ArrayList()
) : LifecycleObserver, Observer<T> {
    init {
        owner.lifecycle.addObserver(this)
        liveData.observeForever(this)
    }

    override fun onChanged(@Nullable t: T) {
        Timber.i("onChanged t=${t.toJson()}")
        if (isActive()) observer.onChanged(t)
        else pending.add(t)
    }

    private fun isActive(): Boolean {
        return owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).also { Timber.i("isActive $it") }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    private fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (owner !== this.owner) { return }
        if (event === Lifecycle.Event.ON_START || event === Lifecycle.Event.ON_RESUME) {
            Timber.i("observer onChanged pending ${pending.toJson()}")
            for (i in pending.indices) {
                observer.onChanged(pending[i])
            }
            pending.clear()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        liveData.removeObserver(this)
        owner.lifecycle.removeObserver(this)
        pending.clear()
    }

    companion object {
        fun <T> bind(@NonNull liveData: LiveData<T>, @NonNull owner: LifecycleOwner, @NonNull observer: Observer<in T>) {
            if (owner.lifecycle.currentState === Lifecycle.State.DESTROYED) return
            LiveEventObserver<T>(liveData, owner, observer)
        }
    }
}
