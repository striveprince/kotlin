package com.lifecycle.demo.inject.data.net.converter

import com.lifecycle.binding.util.beanGet
import com.lifecycle.binding.util.getAllFields
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializerByTypeToken
import okhttp3.FormBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

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
    private val json = Json(JsonConfiguration.Stable.copy(isLenient = true,ignoreUnknownKeys = true))

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        return JsonResponseBodyConverter<Any>(serializerByTypeToken(type), json)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        return JsonRequestBodyConverter<Any>(type)
    }

}

class JsonRequestBodyConverter<T> internal constructor(type: Type) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return if (value is SingleConvert<*>) value.convert() else normalConvert(value)
    }

    private fun normalConvert(value: T): RequestBody {
        return FormBody.Builder()
            .apply {
                val bean = (value as Any)
                val clazzFields = bean.javaClass.getAllFields()
                for (declaredField in clazzFields) {
                    declaredField.beanGet(bean)?.toString()?.let { addEncoded(declaredField.name, it) }
                }
            }
            .build()
    }

}

class JsonResponseBodyConverter<T> internal constructor(
    private val kSerializer: KSerializer<Any>,
    private val json: Json
) : Converter<ResponseBody, T> {

    @InternalSerializationApi
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T = value.use {
        val result = value.string()
        JSONObject(result).let { message ->
            runCatching { json.parse(kSerializer, """{"msg":${message.optString("msg")},code:${message.optInt("code")},"result":$result}""") as T }
                .getOrElse { (json.parse(kSerializer, result) as T) }
        }
    }
}