package com.customers.zktc

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        val d = json.parse(Data.serializer(), jsonString)

//        println(Json.nonstrict.stringify(Data.serializer(),Data(time = Date())))
        println(d.time.time)
//        println(Date().time)
    }

    @Test
    fun t() {
        lateinit var emitter: ObservableEmitter<Long>
        Observable.create<Long> {
            emitter = it
        }
            .firstOrError()
            .subscribe({ println("time:$it") }, { it.printStackTrace() })
        for (index in 0..10){
            Thread.sleep(1000)
            emitter.onNext(System.currentTimeMillis())
        }
    }
}