package com.lifecycle.coroutines.viewmodel.list

interface HttpData<T> {
    suspend fun require(startOffset: Int, it: Int): T

}