package com.lifecycle.demo.inject.data.net.exception

import java.lang.RuntimeException

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/9 17:20
 * Email: 1033144294@qq.com
 */
open class ApiException(val code:String="",msg:String = "") :RuntimeException(msg)