package com.customers.zktc.inject.data.net.converter

import com.binding.model.gson
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
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


class JsonConverterFactory constructor(
    val moshi: Moshi = Moshi.Builder().build(),
    val lenient: Boolean,
    val failOnUnknown: Boolean,
    val serializeNulls: Boolean
) : Converter.Factory() {
    val gson:Gson = Gson()

//    /** Return a new factory which uses [lenient][JsonAdapter.lenient] adapters.  */
//    fun asLenient(): JsonConverterFactory {
//        return JsonConverterFactory(moshi, true, failOnUnknown, serializeNulls)
//    }
//
//    /**
//     * Return a new factory which uses [JsonAdapter.failOnUnknown] adapters.
//     */
//    fun failOnUnknown(): JsonConverterFactory {
//        return JsonConverterFactory(moshi, lenient, true, serializeNulls)
//    }
//
//    /** Return a new factory which includes null values into the serialized JSON.  */
//    fun withNullSerialization(): JsonConverterFactory {
//        return JsonConverterFactory(moshi, lenient, failOnUnknown, true)
//    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        var adapter: JsonAdapter<*> = moshi.adapter<Any>(type, jsonAnnotations(annotations!!))
        if (lenient) adapter = adapter.lenient()
        if (failOnUnknown) adapter = adapter.failOnUnknown()
        if (serializeNulls) adapter = adapter.serializeNulls()
        return MoshiResponseBodyConverter(adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        return JsonRequestBodyConverter<Any>(gson,type)
    }

    private fun jsonAnnotations(annotations: Array<Annotation>): Set<Annotation> {
        var result: MutableSet<Annotation>? = null
        for (annotation in annotations) {
            if(annotation.annotationClass.isSubclassOf(JsonQualifier::class)){
//            if (annotation.annotationType().isAnnotationPresent(JsonQualifier::class.java)) {
                if (result == null) result = LinkedHashSet()
                result.add(annotation)
            }
        }
        return if (result != null) unmodifiableSet<Annotation>(result) else emptySet()
    }
}
