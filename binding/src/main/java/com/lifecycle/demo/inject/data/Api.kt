package com.lifecycle.demo.inject.data

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.demo.inject.data.database.DatabaseApi
import com.lifecycle.demo.inject.data.map.MapApi
import com.lifecycle.demo.inject.data.net.NetApi
import com.lifecycle.demo.inject.data.oss.OssApi
import com.lifecycle.demo.inject.data.preference.PreferenceApi
import com.lifecycle.demo.inject.data.preference.user.UserEntity
//import com.lifecycle.demo.ui.home.page.*
//import com.lifecycle.demo.ui.user.sign.SignParams
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

class Api(
    private val application: Context,
    private val netApi: NetApi,
    private val databaseApi: DatabaseApi,
    private val mapApi: MapApi,
    private val ossApi: OssApi,
    private val preferenceApi: PreferenceApi
) {
//    fun checkUpdate(context: Activity) = netApi.checkUpdate(context)
//
//    fun loginCode(params: SignParams) = netApi.loginCode(params)
//
//    fun modifyPassword() = netApi.modifyPassword()
//
//    fun wechatLogin(params: SignParams) = netApi.wechatLogin(params)
//
//    fun registerCode(mobile: SignParams) = netApi.registerCode(mobile)
//
//    fun passwordLogin(params: SignParams) = netApi.passwordLogin(params)
//        .map { preferenceApi.login(it) }
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun codeLogin(params: SignParams) = netApi.codeLogin(params)
//        .map { preferenceApi.login(it) }
//        .observeOn(AndroidSchedulers.mainThread())
//
//
//    fun register(signParams: SignParams) = netApi.register(signParams)
//        .map { preferenceApi.login(it) }
//        .observeOn(AndroidSchedulers.mainThread())
//
//
//    fun getRecommend(offset: Int, pageCount: Int) = netApi.getRecommend(offset, pageCount)
//
//
//    fun homePage(offset: Int, pageCount: Int, homePageBanner: HomePageBanner) =
//        netApi.homePage(offset, pageCount, homePageBanner)
//
//
//    fun selectHomeTab(currentPosition: Int, position: Int, block: (Int) -> Unit) =
//        when (position) {
//            3 -> preferenceApi.selectHomeTab(currentPosition, position, block)
//            else -> Single.just(position)
//        }
//
//    fun shoppingCartList() = netApi.shoppingCartList()
//
//    fun userBean(): UserEntity {
//        return preferenceApi.userBean()
//    }
//
//    fun getUnReadMessage() =netApi.getUnReadMessage()
//    fun rank()=netApi.rank()
//    fun getCustomerIndexInfo()=netApi.getCustomerIndexInfo()
//    fun logout() = Single.just(true).doOnSuccess { preferenceApi.logout() }
//    fun cities()=netApi.cities()
//    fun location(e: Inflate<*>) =Single.just(true)
//    fun searchGeoCode(activity:AppCompatActivity,obj:(String)->Unit) = mapApi.searchGeoCode(activity,obj)
//    fun locationCurrent(activity: AppCompatActivity) = mapApi.locationCurrent(activity)

}
