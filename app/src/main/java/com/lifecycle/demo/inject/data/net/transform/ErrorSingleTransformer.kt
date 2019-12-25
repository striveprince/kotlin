package com.lifecycle.demo.inject.data.net.transform

import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.inject.data.net.InfoEntity
import com.lifecycle.demo.inject.data.net.exception.ApiException
import com.lifecycle.demo.inject.data.net.exception.AuthenticationException
import com.lifecycle.demo.inject.data.net.exception.LogoutException
import com.lifecycle.demo.inject.data.net.exception.TokenExpireException
import com.lifecycle.binding.util.fromJson
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.SerializationException
import org.json.JSONException
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.*

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:02
 * Email: 1033144294@qq.com
 */
class ErrorSingleTransformer<T> : SingleTransformer<T, T> {
    private val success = 0
    private val tokenExpire = 1
    private val authenticationException = 400
    private val logout = 401
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .onErrorResumeNext { throwable ->
                if(BuildConfig.DEBUG) throwable.printStackTrace()
                var code = 0
                var msg = ""
                when (throwable) {
                    is HttpException -> try {
                        val infoEntity =
                            throwable.response()?.errorBody()!!.string().fromJson<InfoEntity<*>>()
                        code = 0
                        msg = infoEntity.msg
                    } catch (e: Exception) {
                        val thisCode = throwable.code()
                        code =  throwable.code()
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
                    is UnknownHostException ->msg="主机地址错误"
                }
                Single.error(ApiException(code, msg))
            }
            .flatMap {
                Single.create<T> { emitter ->
                    if (it is InfoEntity<*>) {
                        when (it.code) {
                            success -> emitter.onSuccess(it)
                            tokenExpire -> emitter.onError(TokenExpireException(it.code, it.msg))
                            logout -> emitter.onError(LogoutException(it.code, it.msg))
                            authenticationException -> emitter.onError(AuthenticationException(it.code, it.msg))
                            else -> emitter.onError(ApiException(it.code, it.msg))
                        }
                    }
                }
            }
            .onErrorResumeNext { it -> Single.just(it).observeOn(AndroidSchedulers.mainThread()).flatMap { Single.error<T>(it) } }
    }
}