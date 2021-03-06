package com.lifecycle.demo.inject.data.net.converter

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/16 10:20
 * Email: 1033144294@qq.com
 */
interface ApiParams :SingleConvert<RequestBody>{
    override fun convert(): RequestBody {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val value = Gson().toJson(this)
        Timber.i("params=$value")
        return value.toRequestBody(mediaType)
    }
}