package com.customers.zktc.inject.data.net.transform

import android.os.Build
import com.binding.model.fromJson
import com.customers.zktc.BuildConfig
import com.customers.zktc.inject.data.net.InfoEntity
import com.customers.zktc.inject.data.net.exception.AuthenticationException
import com.customers.zktc.inject.data.net.exception.LogoutException
import com.customers.zktc.inject.data.net.exception.ApiException
import com.customers.zktc.inject.data.net.exception.TokenExpireException
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.SerializationException
import org.jetbrains.anko.Android
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.util.*

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:02
 * Email: 1033144294@qq.com
 */
class ErrorSingleTransformer<T> : SingleTransformer<T, T> {
    private val success = "200"
    private val tokenExpire = "1"
    private val authenticationException = "user"
    private val logout = "us"
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .onErrorResumeNext { throwable ->
                if(BuildConfig.DEBUG) throwable.printStackTrace()
                var code = ""
                var msg = ""
                when (throwable) {
                    is HttpException -> try {
                        val infoEntity =
                            throwable.response().errorBody()!!.string().fromJson<InfoEntity<*>>()
                        code = "200"
                        msg = infoEntity.message
                    } catch (e: Exception) {
                        val thisCode = throwable.code()
                        code = "" + throwable.code()
                        when (thisCode) {
                            402 -> "数据库连接错误" + throwable.message()
                            403 -> "无记录" + throwable.message()
                            405 -> "token无效" + throwable.message()
                            400 -> "参数为空" + throwable.message()
                            else -> "网络请求失败" + throwable.message()
                        }
                    }
                    is ServiceConfigurationError -> msg = "服务器错误"
                    is JSONException -> msg = "数据解析错误"
                    is SerializationException ->msg="数据解析错误"
                }
                Single.error(ApiException(code, msg))
            }
            .flatMap {
                Single.create<T> { emitter ->
                    if (it is InfoEntity<*>) {
                        when (it.code) {
                            success -> emitter.onSuccess(it)
                            tokenExpire -> emitter.onError(
                                TokenExpireException(
                                    it.code,
                                    it.message
                                )
                            )
                            logout -> emitter.onError(LogoutException(it.code, it.message))
                            authenticationException -> emitter.onError(
                                AuthenticationException(
                                    it.code,
                                    it.message
                                )
                            )
                            else -> emitter.onError(ApiException(it.code, it.message))

//                        tokenExpire->throw TokenExpireException(it.code,it.message)
//                        logout->throw LogoutException(it.code,it.message)
//                        authenticationException->throw AuthenticationException(it.code,it.message)
                        }
                    }
                }
            }.onErrorResumeNext { it ->
                Single.just(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { Single.error<T>(it) }
            }
    }
}