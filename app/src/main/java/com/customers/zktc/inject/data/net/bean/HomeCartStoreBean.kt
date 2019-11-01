package com.customers.zktc.inject.data.net.bean

import kotlinx.serialization.Serializable

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/30 9:40
 * Email: 1033144294@qq.com
 */

@Serializable
data class HomeShoppingData(
    val storeList:List<HomeCartStoreBean>
)

@Serializable
data class HomeCartStoreBean(
    val couponList:List<CouponListBean>,
    val isThird:Int,
    val storeId:Int,
    val storeName:String,
    val shoppingCartList:List<HomeCartGoodsBean>
)

@Serializable
data class HomeCartGoodsBean(
    val goodsId: Int,
    val goodsInfoAdded: String,
    val goodsInfoAddedTime: String,
    val goodsInfoCostPrice: Double,
    val goodsInfoId: Int,
    val goodsInfoImgId: String,
    val goodsInfoItemNo: String,
    val goodsInfoMarketPrice: Double,
    val goodsInfoName: String,
    val goodsInfoPreferPrice: Double,
    val goodsInfoScorePrice: Double?,
    val goodsInfoStock: Int,
    val goodsInfoSubtitle: String,
    val goodsInfoUnaddedTime: String?,
    val goodsInfoWeight: Int,
    val goodsNum: Int,
    val goodsSpecDetailVos: List<HomeCartGoodsSpecDetailVoBean>,
    val shoppingCartId: Int
)
@Serializable
data class HomeCartGoodsSpecDetailVoBean(
    val specDetailId: Int,
    val specDetailName: String,
    val specDetailNickname: String,
    val specId: Int
)
@Serializable
data class CouponListBean(
    val available: Boolean,
    val couponCateId: Int,
    val couponCode: String,
    val couponId: Int,
    val couponName: String,
    val couponNickname: String,
    val customerId: Int,
    val endTime: String,
    val fullbuyPrice: Int,
    val reason: String,
    val reducePrice: Int,
    val relfOrderId: String?,
    val scopeDescribe: String,
    val scopeList: List<ScopeBean>,
    val scopeType: Int,
    val source: String,
    val startTime: String,
    val thirdId: Int,
    val type: Int,
    val useStatus: Boolean
)
@Serializable
data class ScopeBean(
    val couponId: Int,
    val id: Int,
    val scopeId: Int,
    val scopeType: Int
)