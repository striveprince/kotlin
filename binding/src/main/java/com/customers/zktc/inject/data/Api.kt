package com.customers.zktc.inject.data

import android.content.Context
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi

class Api(
    private val application: Context,
    private val netApi: NetApi,
    private val databaseApi: DatabaseApi,
    private val mapApi: MapApi,
    private val ossApi: OssApi,
    private val preferenceApi: PreferenceApi
) {

}
