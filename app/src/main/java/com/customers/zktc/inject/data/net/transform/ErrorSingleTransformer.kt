package com.customers.zktc.inject.data.net.transform

import com.binding.model.fromGson
import com.customers.zktc.inject.data.net.InfoEntity
import com.customers.zktc.inject.data.net.exception.AuthenticationException
import com.customers.zktc.inject.data.net.exception.LogoutException
import com.customers.zktc.inject.data.net.exception.RestfulException
import com.customers.zktc.inject.data.net.exception.TokenExpireException
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import retrofit2.HttpException
import java.lang.Exception
import java.util.*

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:02
 * Email: 1033144294@qq.com
 */
class ErrorSingleTransformer<T> : SingleTransformer<T, T> {
    private val success ="200"
    private val tokenExpire ="1"
    private val authenticationException ="user"
    private val logout ="us"
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                var code = ""
                var msg = ""
                when (it) {
                    is HttpException -> try {
                        val infoEntity =
                            it.response().errorBody()!!.string().fromGson<InfoEntity<*>>()
                        code = "200"
                        msg = infoEntity.message
                    } catch (e: Exception) {
                        val thisCode = it.code()
                        code = ""+it.code()
                        when (thisCode) {
                            402 -> "数据库连接错误" + it.message()
                            403 -> "无记录" + it.message()
                            405 -> "token无效" + it.message()
                            400 -> "参数为空" + it.message()
                            else -> "网络请求失败" + it.message()
                        }
                    }
                    is ServiceConfigurationError -> msg = "服务器错误"
                    is JSONException -> msg = "数据解析错误"
                }
                Single.error<T>(RestfulException(code, msg))
            }
            .flatMap { Single.create<T> { emitter ->
                if(it is InfoEntity<*>){
                    when(it.code){
                        success->emitter.onSuccess(it)
                        tokenExpire->throw TokenExpireException(it.code,it.message)
                        logout->throw LogoutException(it.code,it.message)
                        authenticationException->throw AuthenticationException(it.code,it.message)
                    }
                }
            } }

    }
}