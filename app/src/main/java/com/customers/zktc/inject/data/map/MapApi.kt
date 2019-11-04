package com.customers.zktc.inject.data.map

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.binding.model.toast
import com.customers.zktc.base.util.checkLocationPermissionWithSetting
import com.customers.zktc.inject.data.net.exception.ApiException
import io.reactivex.Observable
import timber.log.Timber

class MapApi(val context: Context) {
    private val mLocationClient = AMapLocationClient(context)
    private val mLocationOption = AMapLocationClientOption()
    init {
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isNeedAddress = true
        mLocationOption.isOnceLocation = true
        mLocationClient.setLocationOption(mLocationOption)
    }

    private fun location(activity: AppCompatActivity): Observable<AMapLocation> {
        var listener: AMapLocationListener? = null
        return checkLocationPermissionWithSetting(activity)
            .flatMap {
                Observable.create<AMapLocation> { emitter ->
                    if (it ) {
                        listener = AMapLocationListener {
                            Timber.i("address=${it.country}${it.province}${it.city} info ${it.errorInfo}")
                            if (it != null && it.errorCode == 0) {
                                emitter.onNext(it)
                                emitter.onComplete()
                                mLocationClient.stopLocation()
                                mLocationClient.unRegisterLocationListener(listener)
                                return@AMapLocationListener
                            }else{
                                mLocationClient.stopLocation()
                                mLocationClient.unRegisterLocationListener(listener)
                                emitter.onError(ApiException("","无法定位"))
                            }
                        }
                        if(activity.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)){
                            mLocationClient.setLocationListener(listener)
                            mLocationClient.startLocation()
                        } else {
                            mLocationClient.stopLocation()
                            mLocationClient.unRegisterLocationListener(listener)
                        }
                    }
                }
            }
    }

    fun locationCity(activity: AppCompatActivity): Observable<String> {
        return location(activity).map { it.city }
    }
}
