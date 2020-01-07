
package com.lifecycle.demo.inject.data.net.converter

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException

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
        value.string().let { json.parse(kSerializer,result) as T }


    }
}