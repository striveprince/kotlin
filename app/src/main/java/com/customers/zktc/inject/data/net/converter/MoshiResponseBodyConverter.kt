/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.customers.zktc.inject.data.net.converter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

import java.io.IOException

import okhttp3.ResponseBody
import okio.BufferedSource
import okio.ByteString
import retrofit2.Converter

internal class MoshiResponseBodyConverter<T>(private val adapter: JsonAdapter<T>) :
    Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val source = value.source()
        value.use {
            if (source.rangeEquals(0, UTF8_BOM)) {
                source.skip(UTF8_BOM.size().toLong())
            }
            val reader = JsonReader.of(source)
            val result = adapter.fromJson(reader)
            if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
                throw JsonDataException("JSON document was not fully consumed.")
            }
            return result
        }
    }

    companion object {
        private val UTF8_BOM = ByteString.decodeHex("EFBBBF")
    }
}
