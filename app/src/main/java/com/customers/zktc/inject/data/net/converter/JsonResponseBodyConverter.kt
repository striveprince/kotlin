
package com.customers.zktc.inject.data.net.converter

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerByTypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

class JsonResponseBodyConverter<T> internal constructor(
    private val json: Json,
    private val type: Type
) : Converter<ResponseBody, T> {

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T = value.use {

        val serializer = serializerByTypeToken(type)
        return json.parse(serializer,value.string()) as T
    }

}