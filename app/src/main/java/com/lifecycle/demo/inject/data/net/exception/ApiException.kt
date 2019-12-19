package com.lifecycle.demo.inject.data.net.exception

import java.lang.RuntimeException

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/9 17:20
 * Email: 1033144294@qq.com
 */
open class ApiException(val code:Int=0,msg:String = "") :RuntimeException(msg)
open class ApiEmptyException(val code:Int=0,msg:String = "") :RuntimeException(msg)

class AuthenticationException(code:Int=0, msg:String="") :ApiException(code,msg)

class TokenExpireException(code:Int=0,msg:String="") :ApiException(code,msg)

class LogoutException(code:Int=0, msg:String="") :ApiException(code,msg)

class NoPermissionException(code:Int=0, msg:String="请先同意权限再继续操作") :ApiException(code,msg)