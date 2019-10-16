package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.oss.OssEntity
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.page.HomePageBannerData
import com.customers.zktc.ui.home.page.HomePageParams
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignEntity
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


//    用户相关
    @POST("/api/v2/customer/passwdLogin")
    fun passwordLogin( @Body params: SignParams): Single<InfoEntity<SignEntity>>

    @POST("/api/v2/customer/getLoginSmsCode")
    fun loginCode(@Body params: SignParams): Single<InfoEntity<CodeEntity>>

    @POST("/api/v2/customer/getSmsLoginCode")
    fun codeLogin(@Body params: SignParams): Single<InfoEntity<SignEntity>>

    @POST("/api/v2/customer/getRegisteSmsCode")
    fun registerCode(@Body params: SignParams): Single<InfoEntity<CodeEntity>>

    @POST("/api/v2/customer/finishCodeRegiste")
    fun register(@Body params: SignParams): Single<InfoEntity<SignEntity>>

    fun modifyPassword(): Single<String>

    fun wechatLogin(@Body params: SignParams): Single<InfoEntity<UserEntity>>


    @POST("/api/v2/ope/getOperationAd")
    fun banner(@Body params: HomePageParams):Single<InfoEntity<HomePageBannerData>>

}