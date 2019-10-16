package com.customers.zktc.inject.data.net.converter

import com.customers.zktc.ui.user.sign.SignParams
import okhttp3.RequestBody

/**
 * Created by pc on 2017/9/6.
 */

interface SingleConvert<T : RequestBody> {
    fun convert(): T
}
