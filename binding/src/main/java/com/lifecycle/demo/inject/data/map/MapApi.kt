package com.lifecycle.demo.inject.data.map

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
import com.lifecycle.binding.toEntities
import com.lifecycle.demo.inject.data.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MapApi(val context: Context) {
    private val mLocationClient = AMapLocationClient(context)
    private val mLocationOption = AMapLocationClientOption()
//    private val search = NearbySearch.getInstance(context)

    init {
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isNeedAddress = true
        mLocationOption.isOnceLocation = true
        mLocationClient.setLocationOption(mLocationOption)
    }

}
