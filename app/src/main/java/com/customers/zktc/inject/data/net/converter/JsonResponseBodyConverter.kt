
package com.customers.zktc.inject.data.net.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

class JsonResponseBodyConverter<T : Any> internal constructor(
    private val json: Gson,
    private val type: Type
) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        return value.use {
                        val adapter = json.getAdapter<T>(TypeToken.get(type) as TypeToken<T>)
            val jsonReader = json.newJsonReader(it.charStream())
            val t = adapter.read(jsonReader)
            t
        }

    }

}

//