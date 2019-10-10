package com.customers.zktc.inject.data.net.exception

import java.lang.RuntimeException

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/9 17:20
 * Email: 1033144294@qq.com
 */
open class RestfulException(code:String="",msg:String="") :RuntimeException() {
    val code= code
    private val msg= msg
}