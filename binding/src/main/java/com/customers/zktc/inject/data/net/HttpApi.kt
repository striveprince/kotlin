package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface HttpApi {
    @GET("/v1/ossSign")
    fun ossApi(@Query("content") content: String): Single<OssEntity>

    @Headers("Content-Type: application/octet-stream")
    @Streaming
    @GET
    fun download(@Header("RANGE") start: Long, @Header("Config.fileName") fileName: String, @Url path: String): Single<ResponseBody>


    @POST("/api/v2/address/")
    fun location(): Single<InfoEntity<String>>
}