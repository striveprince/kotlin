package com.lifecycle.demo.inject.data.net.exception

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 14:33
 * Email: 1033144294@qq.com
 */
class LogoutException(code:String="", msg:String="") :ApiException(code,msg)