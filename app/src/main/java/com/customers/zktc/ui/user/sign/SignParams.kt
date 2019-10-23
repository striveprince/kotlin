package com.customers.zktc.ui.user.sign

import android.annotation.SuppressLint
import android.os.Parcelable
import com.customers.zktc.base.util.md5
import com.customers.zktc.inject.data.net.converter.ApiParams
import kotlinx.android.parcel.Parcelize
import okhttp3.RequestBody

@SuppressLint("ParcelCreator")
@Parcelize
class SignParams(
    var mobile: String = "",
    var accountName: String = "",
    var smsCode: String = "",
    var uid: String = "",
    var invitationCode: String = "",
    var password: String = "",
    @Transient var confirmPassword: String = ""
) : Parcelable, ApiParams {


    override fun convert(): RequestBody {
        accountName = mobile
        return if (password.isNotEmpty()) {
            confirmPassword = password
            password = md5(password)
            val requestBody = super.convert()
            password = confirmPassword
            requestBody
        } else super.convert()
    }

}