package com.lifecycle.demo.inject.data.preference.user


data class UserEntity (
    var mpAppId: String = "",
    var mpNonceStr: String ="",
    var mpSignature: String="",
    var mpTimestamp: Long = 0,
    var openId: String ="",
    var unionId: String="",
    var token: String="",
    var accountId: Int = 0,
    var accountShowname: String="",
    var accountType: String="",
    var authStatus: String="",
    var customerId: Int=0,
    var customerImg: String="",
    var customerNickname: String="",
    var customerUsername: String="",
    var invitationCode: String="",
    var isVip: Boolean = false,
    var mobile: String="",
    var promotionCode: String="",
    var storeId: Int = 0,
    var userUniqueCode: String=""
)