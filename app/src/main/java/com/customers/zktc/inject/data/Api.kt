package com.customers.zktc.inject.data

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.home.page.*
import com.customers.zktc.ui.user.sign.SignParams
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
    fun checkUpdate(context: Activity) = netApi.checkUpdate(context)

    fun locationCity(activity: AppCompatActivity) = mapApi.locationCity(activity)

    fun loginCode(params: SignParams) = netApi.loginCode(params)

    fun modifyPassword() = netApi.modifyPassword()

    fun wechatLogin(params: SignParams) = netApi.wechatLogin(params)

    fun registerCode(mobile: SignParams) = netApi.registerCode(mobile)

    fun passwordLogin(params: SignParams) = netApi.passwordLogin(params)
        .map { preferenceApi.login(it) }
        .observeOn(AndroidSchedulers.mainThread())

    fun codeLogin(params: SignParams) = netApi.codeLogin(params)
        .map { preferenceApi.login(it) }
        .observeOn(AndroidSchedulers.mainThread())


    fun register(signParams: SignParams) = netApi.register(signParams)
        .map { preferenceApi.login(it) }
        .observeOn(AndroidSchedulers.mainThread())


    fun getRecommend(offset: Int, pageCount: Int) = netApi.getRecommend(offset, pageCount)


    fun homePage(offset: Int, pageCount: Int, homePageBanner: HomePageBanner) =
        netApi.homePage(offset, pageCount, homePageBanner)


    fun selectHomeTab(currentPosition: Int, position: Int, block: (Int) -> Unit) =
        when (position) {
            3 -> preferenceApi.selectHomeTab(currentPosition, position, block)
            else -> Single.just(position)
        }

    fun shoppingCartList() = netApi.shoppingCartList()

    fun userBean(): UserEntity {
        return preferenceApi.userBean()
    }

    fun getUnReadMessage() =netApi.getUnReadMessage()
    fun rank()=netApi.rank()
    fun getCustomerIndexInfo()=netApi.getCustomerIndexInfo()

}
