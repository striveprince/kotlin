package com.lifecycle.demo.inject.data.net.converter

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializerByTypeToken
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：15:18
 * modify developer：  admin
 * modify time：15:18
 * modify remark：
 *
 * @version 2.0
 */


class JsonConverterFactory : Converter.Factory() {
    private val gson:Gson = Gson()
    private val json = Json(JsonConfiguration.Stable.copy(unquoted = true))
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        return JsonResponseBodyConverter<Any>(serializerByTypeToken(type),json)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        return JsonRequestBodyConverter<Any>(gson,type)
    }

}
