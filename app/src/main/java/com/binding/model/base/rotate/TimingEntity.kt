package com.binding.model.base.rotate

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created by pc on 2017/8/29.
 */

class TimingEntity : TimeEntity, LifecycleObserver {
    var time: Int = 0
    var listener: ((Int) -> Unit)? = null


    override fun getTurn() {
        if (--time == 0)
            TimeUtil.instance.remove(this)
        listener?.invoke(time)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        TimeUtil.instance.remove(this)
    }


}
