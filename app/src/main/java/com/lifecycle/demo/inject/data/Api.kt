package com.lifecycle.demo.inject.data

import android.content.Context
import com.lifecycle.demo.base.util.restful
import com.lifecycle.demo.inject.data.database.DatabaseApi
import com.lifecycle.demo.inject.data.map.MapApi
import com.lifecycle.demo.inject.data.net.NetApi
import com.lifecycle.demo.inject.data.net.SignParams
import com.lifecycle.demo.inject.data.oss.OssApi
import com.lifecycle.demo.inject.data.preference.PreferenceApi

class Api(
    private val application: Context,
    val netApi: NetApi,
    val databaseApi: DatabaseApi,
    val mapApi: MapApi,
    val ossApi: OssApi,
    val preferenceApi: PreferenceApi
) {
    fun signIn(sign: SignParams) =
        netApi.httpApi
            .signIn(sign)
            .restful()
            .map { preferenceApi.userApi.login(it) }
}
