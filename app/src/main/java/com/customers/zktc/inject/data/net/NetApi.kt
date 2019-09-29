package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import io.reactivex.Observable

interface NetApi {
    fun ossApi(): Observable<OssEntity>
}