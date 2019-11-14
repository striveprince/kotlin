package com.lifecycle.demo.inject.data.net.converter

import com.google.gson.Gson

import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.Charset

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken as TypeToken1

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：15:04
 * modify developer：  admin
 * modify time：15:04
 * modify remark：
 *
 * @version 2.0
 */


class JsonRequestBodyConverter<T> internal constructor(gson: Gson, type: Type) : Converter<T, RequestBody> {
    @Suppress("UNCHECKED_CAST")
    private val adapter = gson.getAdapter<T>(TypeToken1.get(type) as TypeToken1<T>)!!
    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
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
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }
    }

}
