package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.page.HomePageBannerData
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignParams
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

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
//POST /{version}/customer/getLoginOrRegisteCode
    @POST("/api/v2/customer/getLoginOrRegisteCode")
    fun code(@Field("mobile")mobile:String): Single<InfoEntity<CodeEntity>>
    fun codeLogin(params: SignParams): Single<InfoEntity<UserEntity>>
    fun modifyPassword(): Single<String>
    fun register(signParams: SignParams): Single<InfoEntity<UserEntity>>


}