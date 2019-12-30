package com.lifecycle.binding.rx.observer

import io.reactivex.disposables.Disposable
import io.reactivex.internal.subscriptions.SubscriptionHelper
import org.reactivestreams.Subscription

class FlowableDisposable(val it: Subscription) :Disposable{
    override fun isDisposed(): Boolean {
        return it == SubscriptionHelper.CANCELLED
    }

    override fun dispose() {
        it.cancel()
    }
}