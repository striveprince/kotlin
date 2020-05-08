package com.lifecycle.demo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

//        for (constructor in A::class.java.constructors) {
//        }
        val const = A::class.java.getConstructor(Int::class.java, String::class.java, String::class.java)
        val a = const.newInstance(1,"","")
        print("$a")
    }
}

class A(index:Int,name:String,any: Any){

}
