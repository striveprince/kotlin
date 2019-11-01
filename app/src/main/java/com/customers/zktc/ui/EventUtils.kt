package com.customers.zktc.ui

import android.annotation.SuppressLint
import android.os.Parcelable
import com.binding.model.busPost
import com.binding.model.rxBus
import com.customers.zktc.base.util.md5
import com.customers.zktc.inject.data.net.converter.ApiParams
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.user.sign.SignParams
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/17 9:29
 * Email: 1033144294@qq.com
 */


data class SignEvent(val path: String,val signParams: SignParams)
fun signEvent(path:String, signParams: SignParams){
    signParams.password = ""
    signParams.uid = ""
    signParams.invitationCode = ""
    signParams.confirmPassword = ""
    signParams.smsCode = ""
    busPost(SignEvent(path, signParams))
}

fun receiveSignEvent(): Observable<SignEvent> {
    return rxBus()
}


class LoginEvent(val login: Boolean, val userEntity: UserEntity)

fun loginEvent(boolean: Boolean,userEntity: UserEntity){
    busPost(LoginEvent(boolean, userEntity))
}

fun receiveLoginEvent(): Observable<LoginEvent>{
    return rxBus()
}
