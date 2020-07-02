package com.lifecycle.demo

import com.lifecycle.binding.util.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val json = """{
  "code": 0,
  "msg": "ok",
  "result": {
    "id": "442840854",
    "issues": "awscsa",
    "master": "14e4bad0-b801-48c3-9f7d-0d89e1428439",
    "master_name": "土拨鼠",
    "max_number": 4,
    "observation_uid": "12345678-1234-1234-1234-123456789012",
    "password": "awws"
  }
}"""

    @Test
    fun addition_isCorrect() {
        val bean = json.fromJson<InfoEntity<Bean>>()
        println("class = ${bean.javaClass.simpleName} ${bean.result.issues}")
        assertEquals(4, 2 + 2)
    }

//    inline fun<reified T> String.json():T{
//        return fromJson<T>()
//    }
}
