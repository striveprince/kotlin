package com.customers.zktc.inject.data.net

import com.customers.zktc.inject.data.net.bean.*
import com.customers.zktc.inject.data.oss.OssEntity
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.cart.HomeShoppingParams
import com.customers.zktc.ui.home.page.*
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.user.sign.CodeEntity
import com.customers.zktc.ui.user.sign.SignEntity
import io.reactivex.Observable
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

    //    用户相关
    @POST("/api/v2/customer/passwdLogin")
    fun passwordLogin(@Body params: SignParams): Single<InfoEntity<SignEntity>>

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
    fun getOperationAd(@Body params: HomeOperationParams): Single<InfoEntity<HomePageOperationData>>

    @POST("/api/v2/marketing/rush/list")
//    fun getRushList(@Body params: HomeRecommendParams):Single<InfoEntity<HomeGoodsVoData>>
    fun getRushList(@Body params: HomeRushParams): Single<InfoEntity<HomeGoodsVoData>>

    @POST("/api/v2/marketing/rush/list")
//    fun getRushList(@Body params: HomeRecommendParams):Single<InfoEntity<HomeGoodsVoData>>
    fun getList(@Body params: HomeRushListParams): Single<InfoEntity<HomeGoodsVoData>>

    @POST("/api/v2/ope/getOperationHomeCategory")
    fun operationHomeCategorys(): Single<InfoEntity<HomeCategoryData>>

    @POST("/api/v2/ope/getGoodsRecommend")
    fun homeGoodRecommend(@Body params: HomeRecommendParams): Single<InfoEntity<HomeRecommendData>>
//    D: ---getRequestId url :1072 http://dev.zhongketongchuang.com/api/v2/ope/getGoodsRecommend
//    D: ---onRequest :{"siteNumber":"goods_home_index_1","pageNo":"1","pageNum":"1","pageSize":"200"}

    @POST("/api/v2/ope/getOperationFloor")
    fun getOperationFloor(@Body params: HomeFloorParams): Single<InfoEntity<HomeFloorData>>

    @POST("/api/v2/marketing/group/list")
    fun marketingList(@Body homeRushListParams: HomeRushListParams): Single<InfoEntity<HomeGoodsVoData>>

    @POST("/api/v2/goods/recommendGoodsList")
    fun getRecommendGoodsList(@Body homeGoodsRecommend:HomeGoodsRecommend): Single<InfoEntity<HomePageRecommendData>>

    @POST("/api/v2/shoppingcart/getShoppingCart")
    fun shoppingCartList(@Body homeShoppingParams: HomeShoppingParams):Single<InfoEntity<HomeShoppingData>>
//    POST /{version}/shoppingcart/getShoppingCart
}