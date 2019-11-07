package com.customers.zktc.inject.data.map

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.nearby.NearbySearch
import com.binding.model.toEntities
import com.customers.zktc.base.util.checkLocationPermissionWithSetting
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.ui.user.address.MapEntity
import io.reactivex.Observable
import timber.log.Timber

class MapApi(val context: Context) {
    private val mLocationClient = AMapLocationClient(context)
    private val mLocationOption = AMapLocationClientOption()
    private val search = NearbySearch.getInstance(context)

    init {
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isNeedAddress = true
        mLocationOption.isOnceLocation = true
        mLocationClient.setLocationOption(mLocationOption)
    }

    fun locationCurrent(activity: AppCompatActivity): Observable<AMapLocation> {
        var listener: AMapLocationListener? = null
        return checkLocationPermissionWithSetting(activity)
            .flatMap {
                Observable.create<AMapLocation> { emitter ->
                    if (it) {
                        listener = AMapLocationListener {
                            Timber.i("address=${it.country}${it.province}${it.city} info ${it.errorInfo}")
                            if (it != null && it.errorCode == 0&&activity.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                                emitter.onNext(it)
                                emitter.onComplete()
                                mLocationClient.stopLocation()
                                mLocationClient.unRegisterLocationListener(listener)
                                return@AMapLocationListener
                            } else {
                                mLocationClient.stopLocation()
                                mLocationClient.unRegisterLocationListener(listener)
                                emitter.onError(ApiException("${it.errorCode}+${it.errorInfo}", "无法定位"))
                            }
                        }
                        if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
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

    fun searchGeoCode(activity: AppCompatActivity, obj: (String) -> Unit):Observable<List<MapEntity>> {
        val coderSearch = GeocodeSearch(activity)
        return locationCurrent(activity)
            .map { LatLonPoint(it.latitude, it.longitude) }
            .flatMap { createCoderSearch(it,coderSearch,obj) }
//            .map { it.toEntities<MapEntity>() }
    }

    private fun createCoderSearch(
        latLonPoint: LatLonPoint,
        coderSearch: GeocodeSearch,
        obj: (String) -> Unit
    ): Observable<List<MapEntity>> {
        return Observable.create<List<MapEntity>> {
            coderSearch.setOnGeocodeSearchListener(
                object : GeocodeSearch.OnGeocodeSearchListener {
                    override fun onRegeocodeSearched(p: RegeocodeResult, p1: Int) {
                        obj.invoke(p.regeocodeAddress.formatAddress)
                        val province = p.regeocodeAddress.province
                        val city = p.regeocodeAddress.city
                        val district= p.regeocodeAddress.district
                        it.onNext(p.regeocodeAddress.pois.toEntities(province,city,district))
                        it.onComplete()
                    }
                    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {}
                })
            coderSearch.getFromLocationAsyn(RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP))
        }
    }
}
