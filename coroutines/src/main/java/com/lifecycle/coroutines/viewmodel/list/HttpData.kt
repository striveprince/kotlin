package com.lifecycle.coroutines.viewmodel.list

import kotlinx.coroutines.flow.Flow

interface HttpData<T> {
    suspend fun require(startOffset: Int, it: Int): Flow<T>

}