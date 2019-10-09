package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import java.io.File

interface NetApi {
    fun ossApi(): Single<OssEntity>
    fun download(path:String):Single<ResponseBody>
}