package com.lifecycle.demo.inject.data

import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.inject.data.net.InfoEntity
import com.lifecycle.binding.util.fromJson
import kotlinx.serialization.SerializationException
import org.json.JSONException
import retrofit2.HttpException
import java.lang.RuntimeException
import java.net.UnknownHostException
import java.util.*

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/9 17:20
 * Email: 1033144294@qq.com
 */

const val success = 0
const val tokenExpire = 401
const val authenticationException = 402
const val logout = 1

open class ApiException(val code: Int = 0, msg: String = "", val obj: InfoEntity<*>? = null) : RuntimeException(msg)

open class ApiEmptyException(code: Int = 0, msg: String = "", obj: InfoEntity<*>? = null) : ApiException(code, msg, obj)

class AuthenticationException(code: Int = authenticationException, msg: String = "", obj: InfoEntity<*>? = null) : ApiException(code, msg, obj)

class TokenExpireException(code: Int = tokenExpire, msg: String = "", obj: InfoEntity<*>? = null) : ApiException(code, msg, obj)

class LogoutException(code: Int = logout, msg: String = "", obj: InfoEntity<*>? = null) : ApiException(code, msg, obj)

class NoPermissionException(code: Int = 0, msg: String = "请先同意权限再继续操作", obj: InfoEntity<*>? = null) : ApiException(code, msg, obj)

fun judgeThrowable(it: Throwable): ApiException {
    return when (it) {
        is HttpException -> ApiException(it.code(), httpErrorMessage(it), infoEntity(it))
        is ServiceConfigurationError -> ApiException(0, "服务器错误")
        is JSONException -> ApiException(0, "数据解析错误")
        is SerializationException -> ApiException(0, "数据解析错误")
        is UnknownHostException -> ApiException(0, "无网络")
        is ApiException -> it
        else -> ApiException(0, it.message ?: "")
    }
}

fun <T> judgeApiThrowable(it: InfoEntity<T>): ApiException {
    return when (it.code) {
        tokenExpire -> TokenExpireException(it.code, httpErrorMessage(it), it)
        authenticationException -> AuthenticationException(it.code, httpErrorMessage(it), it)
        logout -> LogoutException(it.code, httpErrorMessage(it), it)
        else -> ApiException(it.code, httpErrorMessage(it), it)
    }
}

fun dealWithException(it: Throwable): Throwable {
    when (it) {
        is AuthenticationException -> ARouterUtil.signIn()
    }
    return it
}

fun infoEntity(it: HttpException): InfoEntity<*>? {
    return it.response()?.errorBody()?.use { it.string().fromJson() }
}

fun httpErrorMessage(it: HttpException): String {
    return when (it.code()) {
        401 -> "登录验证过期"
        402 -> "数据库连接错误" + it.message()
        403 -> "无记录" + it.message()
        405 -> "token无效" + it.message()
        400 -> "参数为空" + it.message()
        404 -> "无法找到资源" + it.message()
        else -> "网络请求失败" + it.message()
    }
}

fun httpErrorMessage(infoEntity: InfoEntity<*>): String {
    return when (infoEntity.code) {
        401 -> "登录验证过期"
        402 -> "数据库连接错误"
        403 -> "无记录"
        405 -> "token无效"
        400 -> "参数为空"
        404 -> "无法找到资源"
        else -> infoEntity.msg
    }
}