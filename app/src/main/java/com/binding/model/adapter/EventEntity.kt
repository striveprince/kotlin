package com.binding.model.adapter

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/5 16:44
 * Email: 1033144294@qq.com
 */
data class EventEntity<T>(val success:Boolean = true, val result:T?=null)