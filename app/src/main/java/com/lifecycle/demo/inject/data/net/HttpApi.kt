package com.lifecycle.demo.inject.data.net

import com.lifecycle.demo.inject.InfoEntity
import com.lifecycle.demo.inject.data.net.bean.InterrogationDataBean
import com.lifecycle.demo.inject.data.oss.OssEntity
import com.lifecycle.demo.inject.data.net.bean.TokenBean
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

    @POST("/login")
    fun signIn(@Body value: SignParams): Single<InfoEntity<TokenBean>>

    @POST("/getList")
    fun getInterrogationRxList(@Body value: InterrogationParams): Single<InfoEntity<InterrogationDataBean>>

    @POST("/getList")
    suspend fun getInterrogationList(@Body value: InterrogationParams): InfoEntity<InterrogationDataBean>
}