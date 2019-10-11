package com.customers.zktc.base.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.customers.zktc.R
import com.customers.zktc.inject.data.net.exception.ApiException
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import java.io.File

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
fun  checkPermission(
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




fun  checkLocationPermissionWithSetting(activity: AppCompatActivity): Observable<Boolean> {
    val permission = checkPermissions(activity, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    return if (permission) Observable.just(true)
    else RxPermissions(activity)
        .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        .flatMap {
            return@flatMap if (it) Observable.just(true)
            else dialogForSetting(activity)
        }
}

fun dialogForSetting(activity: AppCompatActivity): Observable<Boolean> {
   return Observable.create<Boolean>{
       if(activity.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
           AlertDialog.Builder(activity)
               .setTitle(R.string.location)
               .setMessage(R.string.gps_notify_msg)
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