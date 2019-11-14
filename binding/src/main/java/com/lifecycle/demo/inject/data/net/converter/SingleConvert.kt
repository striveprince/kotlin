package com.lifecycle.demo.inject.data.net.converter

import okhttp3.RequestBody

/**
 * Created by pc on 2017/9/6.
 */

interface SingleConvert<T : RequestBody> {
    fun convert(): T
}
