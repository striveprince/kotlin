package com.customers.zktc.inject.data.map

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption

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
