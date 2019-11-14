
package com.lifecycle.demo.inject.data.net.converter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

class JsonResponseBodyConverter<T> internal constructor(
    private val kSerializer: KSerializer<Any>,
    private val json: Json
) : Converter<ResponseBody, T> {

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T = value.use {
        json.parse(kSerializer,value.string()) as T
    }

}