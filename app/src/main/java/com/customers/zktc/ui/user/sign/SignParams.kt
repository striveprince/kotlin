package com.customers.zktc.ui.user.sign

import android.annotation.SuppressLint
import android.os.Parcelable
import com.customers.zktc.base.util.md5
import com.customers.zktc.inject.data.net.converter.ApiParams
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class SignParams (
    var mobile:String="",
    var accountName:String = "",
    var smsCode:String = "",
    var uid:String = "",
    var invitationCode:String = "",
    var password:String = "",
    @Transient var confirmPassword:String = ""
): Parcelable, ApiParams {
    fun change(): SignParams {
        accountName = mobile
        password = md5(confirmPassword)
        return this
    }
}