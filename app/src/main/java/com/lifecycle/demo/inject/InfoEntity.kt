package com.lifecycle.demo.inject

import kotlinx.serialization.Serializable

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:19
 * Email: 1033144294@qq.com
 */
@Serializable
data class InfoEntity<T>(
    val result: T?,
    val msg: String = "",
    val code: Int = 0

)