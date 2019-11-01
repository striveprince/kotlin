package com.customers.zktc.inject.data.net.bean

import kotlinx.serialization.Serializable
/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/30 11:35
 * Email: 1033144294@qq.com
 */
@Serializable
data class HomePageOperationBean(
    val id: Int = 0,
    val name: String = "",
    val picture: String = "",
    val viceName: String = "",
    val orderBy: Int = 0,
    val linkUrl: String = ""
)

@Serializable
data class HomePageRecommendData(
    val storeInfo:StoreInfoBean,
    val recommendGoodsList:List<HomeGoodsRecommendListBean>
)

@Serializable
data class StoreInfoBean(
    val logoUrl:String,
    val storeName:String,
    val storeIntroduce:String
)

@Serializable
data class HomePageOperationData(val operationAds: List<HomePageOperationBean>)

@Serializable
data class HomeCategoryBean(
    val iconUrl: String,
    val linkUrl: String,
    val name: String,
    val orderBy: Int
)

@Serializable
data class HomeCategoryData(val operationHomeCategorys: List<HomeCategoryBean>)


@Serializable
data class HomeFloorData(
    val operationFloorTypes: List<HomeFloorDataBean>
)

@Serializable
data class HomeFloorDataBean(
    val name: String,
    val pictureNumber: Int = 1,
    val operationFloors: List<HomeFloorTypeBean>
)

@Serializable
data class HomeFloorTypeBean(
    val floorLinkUrl: String,
    val floorName: String,
    val floorPicture: String,
    val floorViceName: String,
    val id: Int,
    val orderBy: Int
)


@Serializable
data class HomeGoodsVoData(
    val goodsVos: List<HomeGoodVosBean>
)
@Serializable
data class HomeGoodVosBean(
    val goodsVo: HomeGoodVoBean,
    val marketingTimeDesc: String,
    val marketingTime: String,
    val marketingProgress: Double,
    val marketingVo: MarketVoBean
)

@Serializable
data class HomeGoodVoBean(
    val auditStatus: String,
    val fictitiousSalesCount: Int,
    val freightTemplateId: Int?,
    val goodsBrandVo: Int?,
    val goodsId: Int,
    val goodsInfoAdded: String,
    val goodsInfoAddedTime: String,
    val goodsInfoBarcode: String,
    val goodsInfoCostPrice: Double,
    val goodsInfoId: Int,
    val goodsInfoImgId: String,
    val goodsInfoIsbn: String,
    val goodsInfoItemNo: String,
    val goodsInfoMarketPrice: Double,
    val goodsInfoName: String,
    val goodsInfoPreferPrice: Double,
    val goodsInfoScorePrice: Int?,
    val goodsInfoStock: Int,
    val goodsInfoSubtitle: String,
    val goodsInfoUnaddedTime: Int?,
    val goodsInfoWeight: Double,
    val goodsSpecDetailVos: List<String>,
    val isCustomerDiscount: String,
    val isThird: String,
    val ismailbay: String,
    val offlineflag: Int,
    val refuseReason: String,
    val showList: String,
    val showMobile: String,
    val thirdId: Int,
    val thirdName: String
)

@Serializable
data class MarketVoBean(
    val groupedNumbers: Int,
    val marketingId: Int,
    val marketingName: String,
    val marketingPrice: Double,
    val marketingType: Int,
    val marketingVipPrice: Double,
    val ruleId: Int,
    val ruleName: String,
    val storeId: Int
)

//推荐界面
@Serializable
data class HomeRecommendData(
    val goodsRecommends: List<HomeGoodsRecommendBean>,
    val haveNext: Boolean
)
@Serializable
data class HomeGoodsRecommendBean(
    val goodsId: Int,
    val goodsName: String,
    val goodsPicture: String,
    val preferPrice: Double,
    val scorePrice: String?
)

@Serializable
data class HomeGoodsRecommendListBean(
    val goodsId: Int,
    val goodsName: String,
    val goodsImg: String,
    val goodsDetailDesc: String
)