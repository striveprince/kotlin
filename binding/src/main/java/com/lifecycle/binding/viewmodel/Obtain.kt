package com.lifecycle.binding.viewmodel

interface Obtain<T,Job> {
    fun onSubscribe(job:Job)
    fun onNext(t:T)
    fun onError(e:Throwable)
    fun onComplete()
}