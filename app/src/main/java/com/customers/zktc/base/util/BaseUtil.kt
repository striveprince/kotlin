package com.customers.zktc.base.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.customers.zktc.R
import com.customers.zktc.inject.data.net.InfoEntity
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.inject.data.net.transform.ErrorSingleTransformer
import com.customers.zktc.inject.data.net.transform.NoErrorObservableTransformer
import com.customers.zktc.inject.data.net.transform.RestfulSingleTransformer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.security.MessageDigest
import java.util.regex.Pattern


/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 11:09
 * Email: 1033144294@qq.com
 */


fun checkPermissions(activity: Activity, vararg permissions: String): Boolean {
    for (permission in permissions) {
        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (ActivityCompat.checkSelfPermission(
                activity,
                permission
            ) !== PackageManager.PERMISSION_GRANTED
        )
            return false
    }
    return true
}



fun headUrl(url: String): GlideUrl {
    var url = url
    if (TextUtils.isEmpty(url)) url = "1"
    return GlideUrl(
        url, LazyHeaders.Builder()
            //.addHeader("referer", "")
            .build()
    )
}

fun md5(paramString: String): String {
    return try {
        val arrayOfByte = MessageDigest.getInstance("MD5")
            .digest(paramString.toByteArray(charset("UTF-8")))
        val stringBuilder = StringBuilder()
        for (b in arrayOfByte.indices) {
            val str = Integer.toHexString(arrayOfByte[b].toInt() and 0xFF)
            if (str.length == 1)
                stringBuilder.append("0")
            stringBuilder.append(str)
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        ""
    }

}

fun<T> Single<T>.noError():Observable<T>{
    return this.toObservable().compose(NoErrorObservableTransformer())
}

fun<T> Observable<T>.noError():Observable<T>{
    return this.compose(NoErrorObservableTransformer())
}


fun <T> Single<InfoEntity<T>>.restful(): Single<T> {
    return this.compose(ErrorSingleTransformer())
        .compose(RestfulSingleTransformer())
}


fun <T> Single<InfoEntity<T>>.restfulCompose(): Single<T> {
    return this.restful()
        .observeOn(AndroidSchedulers.mainThread())
}
fun <T> Single<T>.errorCompose(): Single<T> {
    return this.compose(ErrorSingleTransformer())
        .observeOn(AndroidSchedulers.mainThread())
}

//fun <T> Single<T>.errorCompose(): Single<T> {
//    return this.subscribeOn(Schedulers.io().asCoroutineDispatcher().scheduler)
//        .compose(ErrorSingleTransformer())
//        .observeOn(AndroidSchedulers.mainThread().asCoroutineDispatcher().scheduler)
//}

fun <T> Observable<T>.checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<T> {
    return if (checkPermissions(activity, *permissions)) this
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) this
            else Observable.error(
                ApiException("", activity.getString(R.string.no_permission))
            )
        }
}

fun checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<Boolean> {
    return if (checkPermissions(activity, *permissions)) Observable.just(true)
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) Observable.just(true)
            else Observable.error(
                ApiException("", activity.getString(R.string.no_permission))
            )
        }
}

fun selectFile(activity: AppCompatActivity, type: String): Observable<File> {
    val intent = Intent(activity, PhoneSystemManager::class.java)
    intent.putExtra(PhoneSystemManager.type, type)
    activity.startActivity(intent)
    return Observable.just(0)
        .checkPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        .flatMap { _ -> Observable.create<Any> { PhoneSystemManager.emitter = it } }
        .filter { it is File }
        .map { it as File }
}


fun checkLocationPermissionWithSetting(activity: AppCompatActivity): Observable<Boolean> {
    val permission = checkPermissions(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    return if (permission) Observable.just(true)
    else RxPermissions(activity)
        .request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        .flatMap {
            return@flatMap if (it) Observable.just(true)
            else dialogForSetting(activity)
        }
}

fun dialogForSetting(activity: AppCompatActivity): Observable<Boolean> {
    return Observable.create<Boolean> {
        if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
            AlertDialog.Builder(activity)
                .setTitle(R.string.location)
                .setMessage(R.string.should_open_gps)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    it.onError(ApiException(activity.getString(R.string.should_open_gps)))
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.setting) { dialog, _ ->
                    it.onNext(true)
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
    }.settingLocationPermission(activity)
}

private fun Observable<Boolean>.settingLocationPermission(activity: AppCompatActivity): Observable<Boolean> {
    return Observable.create<Any> { PhoneSystemManager.emitter = it }
        .map { it as Boolean }
        .flatMap {
            if (it) this
            else Observable.error(ApiException("", activity.getString(R.string.no_permission)))
        }
}

const val phone = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$"
fun getPhoneError(mobiles: String): String? {
    if (TextUtils.isEmpty(mobiles)) return "手机号不能为空"
    val p = Pattern.compile(phone)
    val m = p.matcher(mobiles)
    val valid = m.matches()
    return if (valid) null else "不合法的手机号"
}

fun getCodeError(code:String):String?{
    if (TextUtils.isEmpty(code)) return "验证码不能为空"
    return if (code.length in 4..6)null else "验证码长度为4-6位"
}

fun getPasswordError(password:String):String?{
    if (TextUtils.isEmpty(password)) return "密码不能为空"
    return if (password.length>5)null else "密码长度不能小于6位"
}

fun showInputMethod(context: Context):Boolean{
    return if(isShowing(context)) false
    else{
        showOrHide(context)
        true
    }
}

fun showOrHide(context: Context) {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}


fun isShowing(context: Context): Boolean {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isActive
}