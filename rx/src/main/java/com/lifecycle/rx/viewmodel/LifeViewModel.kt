package com.lifecycle.rx.viewmodel

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.Init
import com.lifecycle.rx.observer.FlowableDisposable
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.ListCompositeDisposable

@Suppress("UNCHECKED_CAST")
open class LifeViewModel : ViewModel(), Init {
    internal val disposables = ListCompositeDisposable()
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        attachData(owner,  bundle)
    }

    fun <T> Single<T>.doSubscribe(v: View? = null,value:ValueAnimator?=null):Single<T> {
        v?.isEnabled = false
        value?.start()
        return this.doOnSubscribe { disposables.add(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                v?.isEnabled = true
                value?.let {
                    it.end()
                    it.removeAllListeners()
                    it.removeAllUpdateListeners()
                }
            }
    }

    fun <T> Observable<T>.doSubscribe(): Observable<T> =
        this.doOnSubscribe { disposables.add(it) }

    fun <T> Maybe<T>.doSubscribe() =
        this.doOnSubscribe { disposables.add(it) }

    fun <T> Flowable<T>.doSubscribe(): Flowable<T> =
        this.doOnSubscribe { disposables.add(FlowableDisposable(it)) }

    fun <T> Completable.doSubscribe(): Completable =
        this.doOnSubscribe { disposables.add(it) }


    /**
     * It's not recommended to use it init data
     * @param owner it have activity_video object,please don't give it to property otherwise,it will leak
     * */
    open fun attachData(owner: LifecycleOwner, bundle: Bundle?) {}

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}