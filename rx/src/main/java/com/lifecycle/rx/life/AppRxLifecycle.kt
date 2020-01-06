package com.lifecycle.rx.life

import android.app.Application
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.rx.bus.RxBus
import io.reactivex.Observable

class AppRxLifecycle(
    application: Application,
    parse: Int = 1,
    vm: Int = 2
) : AppLifecycle(application, RxBus.getInstance(), parse, vm)

inline fun <reified E> bus(): Observable<E> {
    return RxBus.getInstance().ofType(E::class.java)
}