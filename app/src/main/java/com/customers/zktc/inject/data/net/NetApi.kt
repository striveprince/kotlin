package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.page.HomePageBannerData
import com.customers.zktc.ui.home.page.HomePageBannerEntity
import com.customers.zktc.ui.user.sign.SignParams
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*
import java.io.File

interface NetApi {
    @GET("/v1/ossSign")
    fun ossApi(@Query("content") content: String): Single<OssEntity>

    @Headers("Content-Type: application/octet-stream")
    @Streaming
    @GET
    fun download(@Header("RANGE") start: Long, @Header("Config.fileName") fileName: String, @Url path: String): Single<ResponseBody>

    @GET("/api/v2/ope/getOperationAd")
    fun banner():Single<InfoEntity<HomePageBannerData>>

    fun passwordLogin(params: SignParams?): Single<InfoEntity<UserEntity>>

    fun wechatLogin(params: SignParams?): Single<InfoEntity<UserEntity>>


}