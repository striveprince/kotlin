package com.customers.zktc.inject.data.net.bean

import kotlinx.serialization.Serializable

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/4 11:28
 * Email: 1033144294@qq.com
 */

@Serializable
data class RankBean(
    val availableCommission: Int,
    val commission: Int,
    val commonTotal: Int,
    val depositAmount: Int,
    val distributionOrderAmount: String?,
    val distributionOrderTotal: Int,
    val proposedCommission: Int,
    val vipTotal: Int
)

@Serializable
data class CustomerIndexBean(
    val authenticate: Boolean,
    val backMsgCount: Int,
    val commentNum: Int,
    val couponCount: Int,
    val customerId: Int,
    val customerImg: String,
    val customerNickname: String,
    val depositBalance: Int,
    val membershipLevel: String,
    val membershipLevelName: String,
    val platFormStoreId: Int,
    val pointBalance: Int,
    val promotionCode: String,
    val virtualStatus: Int,
    val virtualStoreInfo: String?,
    val waitPayMsgCount: Int,
    val waitReceiveMsgCount: Int,
    val waitSendMsgCount: Int
){
    private fun countToString(it: Int) = if (it == 0) "" else it.toString()

}
