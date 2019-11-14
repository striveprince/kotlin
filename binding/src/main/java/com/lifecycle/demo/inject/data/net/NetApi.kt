package com.lifecycle.demo.inject.data.net


/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/29 14:54
 * Email: 1033144294@qq.com
 */
class NetApi(private val httpApi: HttpApi) {


    fun location()=httpApi.location().map{}
}