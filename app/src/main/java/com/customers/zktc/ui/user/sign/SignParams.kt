package com.customers.zktc.ui.user.sign

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 10:29
 * Email: 1033144294@qq.com
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class SignParams (
    var mobile:String = "",
    var smsCode:String = "",
    var uid:String = "",
    var invitationCode:String = "",
    var password:String = "",
    @Transient var confirmPassword:String = ""
): Parcelable