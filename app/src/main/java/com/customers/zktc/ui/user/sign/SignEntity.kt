package com.customers.zktc.ui.user.sign

import kotlinx.serialization.Serializable

@Serializable
data class SignEntity(
    val customerVo: CustomerVo,
    val needBindMobile: Boolean,
    val needBindPhone: Boolean,
    val token: String
)

@Serializable
data class ExtWechatCustomerVo(
    val mpAppId: String,
    val mpNonceStr: String,
    val mpSignature: String,
    val mpTimestamp: Long?,
    val openId: String,
    val unionId: String
)
@Serializable
data class Account(
    val accountId: Int,
    val accountShowname: String,
    val accountType: String
)
@Serializable
data class CustomerVo(
    val accountList: List<Account>,
    val authStatus: String,
    val customerId: Int,
    val customerImg: String,
    val customerNickname: String,
    val customerUsername: String,
    val extWechatCustomerVo: ExtWechatCustomerVo,
    val invitationCode: String,
    val isVip: Boolean,
    val mobile: String,
    val promotionCode: String,
    val storeId: Int?,
    val userUniqueCode: String
)