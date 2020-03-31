package com.lifecycle.demo.inject.data.net.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializerByTypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.Charset

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
//    private val json = Json(JsonConfiguration.Stable.copy(unquoted = true))
    private val json = Json(JsonConfiguration.Stable.copy())

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
class JsonRequestBodyConverter<T> internal constructor(gson: Gson, type: Type) : Converter<T, RequestBody> {
    @Suppress("UNCHECKED_CAST")
    private val adapter = gson.getAdapter<T>(TypeToken.get(type) as TypeToken<T>)!!
    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
        private val UTF_8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return if(value is SingleConvert<*>) value.convert() else normalConvert(value)
    }

    private fun normalConvert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        writer.use {
            adapter.toJson(writer, value)
            return buffer.readByteString().toRequestBody(MEDIA_TYPE)
        }
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
//        val jsonObject = JSONObject(result)
//        val msg = jsonObject.optString("msg")
//        val code = jsonObject.optInt("code")
//        val infoJson = """{"msg":$msg,code:$code,"result":$result}"""
        json.parse(kSerializer,result) as T
    }
}