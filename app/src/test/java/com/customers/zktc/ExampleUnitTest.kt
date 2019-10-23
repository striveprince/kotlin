package com.customers.zktc

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val jsonString = """{"time":"23/10/2019 16:54:12.232"}"""

    @Test
    fun json() {
//        dd/MM/yyyy HH:mm:ss.SSS
        val json = Json(JsonConfiguration.Stable.copy(unquoted = false))
        val d = json.parse(Data.serializer(),jsonString)

//        println(Json.nonstrict.stringify(Data.serializer(),Data(time = Date())))
        println(d.time.time)
//        println(Date().time)

    }
}