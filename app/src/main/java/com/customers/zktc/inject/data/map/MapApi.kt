package com.customers.zktc.inject.data.map

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.customers.zktc.base.util.checkLocationPermissionWithSetting
import com.customers.zktc.inject.data.net.exception.ApiException
import io.reactivex.Observable

class MapApi(val context: Context) {
    private val mLocationClient = AMapLocationClient(context)
    private val mLocationOption = AMapLocationClientOption()
    private fun location(activity: AppCompatActivity): Observable<AMapLocation> {
        var listener: AMapLocationListener? = null
        return checkLocationPermissionWithSetting(activity)
            .flatMap {
                Observable.create<AMapLocation> { emitter ->
                    if (it && activity.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                        listener = AMapLocationListener {
                            if (it != null && it.errorCode == 0) {
                                emitter.onNext(it)
                                emitter.onComplete()
                            }else{
                                emitter.onError(ApiException("","无法定位"))
                            }
                            mLocationClient.stopLocation()
                            mLocationClient.unRegisterLocationListener(listener)
                        }
                        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
                        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                        mLocationClient.setLocationListener(listener)
                        mLocationClient.startLocation()
                    }
                }
            }
    }

    fun locationCity(activity: AppCompatActivity): Observable<String> {
        return location(activity).map { it.city }
    }
}
