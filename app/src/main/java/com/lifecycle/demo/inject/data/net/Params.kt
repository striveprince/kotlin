package com.lifecycle.demo.inject.data.net

import com.lifecycle.demo.inject.data.net.converter.ApiParams

data class SignParams(var login_name: String, var password: String) : ApiParams
data class InterrogationParams(val task_category:Int,val pageIndex:Int = 1,val pageSize:Int = 10): ApiParams


