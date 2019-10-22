package com.customers.zktc.inject.data.net.converter

import com.binding.model.gson
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Collections.unmodifiableSet
import java.util.LinkedHashSet
import kotlin.reflect.full.isSubclassOf

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

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        val json = Json(JsonConfiguration.Stable.copy(unquoted = true))
        return JsonResponseBodyConverter<Any>(json,type)
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
