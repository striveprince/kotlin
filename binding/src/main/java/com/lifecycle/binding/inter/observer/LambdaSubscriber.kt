/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.lifecycle.binding.inter.observer

import java.util.concurrent.atomic.AtomicReference

import io.reactivex.internal.functions.Functions
import io.reactivex.observers.LambdaConsumerIntrospection
import org.reactivestreams.Subscription

import io.reactivex.FlowableSubscriber
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.*
import io.reactivex.functions.*
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.plugins.RxJavaPlugins

class LambdaSubscriber<T>(
    internal val onNext: Consumer<in T>, internal val onError: Consumer<in Throwable>,
    internal val onComplete: Action,
    internal val onSubscribe: Consumer<in Subscription>
) : AtomicReference<Subscription>(), FlowableSubscriber<T>, Subscription, Disposable,
    LambdaConsumerIntrospection {

    override fun onSubscribe(s: Subscription) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                s.cancel()
                onError(ex)
            }

        }
    }

    override fun onNext(t: T) {
        if (!isDisposed) {
            try {
                onNext.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                get().cancel()
                onError(e)
            }

        }
    }

    override fun onError(t: Throwable) {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                onError.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }

        } else {
            RxJavaPlugins.onError(t)
        }
    }

    override fun onComplete() {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                onComplete.run()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }

        }
    }

    override fun dispose() {
        cancel()
    }

    override fun isDisposed(): Boolean {
        return get() === SubscriptionHelper.CANCELLED
    }

    override fun request(n: Long) {
        get().request(n)
    }

    override fun cancel() {
        SubscriptionHelper.cancel(this)
    }

    override fun hasCustomOnError(): Boolean {
        return onError !== Functions.ON_ERROR_MISSING
    }

    companion object {

        private val serialVersionUID = -7251123623727029452L
    }
}
