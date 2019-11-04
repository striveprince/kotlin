package com.customers.zktc.inject.data.preference

import android.content.Context
import com.binding.model.rxBus
import com.customers.zktc.inject.data.preference.setting.SettingApi
import com.customers.zktc.inject.data.preference.user.UserApi
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.ui.LoginEvent
import com.customers.zktc.ui.user.sign.SignEntity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PreferenceApi(val context: Context) {
    private val userApi: UserApi = UserApi.getInstance(context)
    private val settingApi: SettingApi = SettingApi.getInstance(context)
    fun login(it: SignEntity): UserEntity {
        return userApi.login(it)
    }

    fun isLogin()=userApi.login

    fun selectHomeTab(currentPosition: Int, position: Int, block: (Int) -> Unit): Single<Int> {
        return if (userApi.login) {
            Single.just(position)
        } else {
            block.invoke(currentPosition)
            Single.just(position)
                .subscribeOn(Schedulers.newThread())
                .flatMap { rxBusLogin(currentPosition, it) }
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun rxBusLogin(currentPosition: Int, position: Int): Single<Int> {
        return rxBus<LoginEvent>()
            .firstOrError()
            .flatMap { Single.just(if (it.login) position else currentPosition) }
    }

    fun userBean()=userApi.userEntity

}