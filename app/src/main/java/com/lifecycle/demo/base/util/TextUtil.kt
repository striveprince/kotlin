package com.lifecycle.demo.base.util

import java.lang.StringBuilder

class Text(
    val text: String = "",
    val color:Long = -1L,
    val big:Int = 0,
    val line:Boolean = false
) {

    override fun toString(): String {
        val builder = StringBuilder()

        return builder.toString()
    }
}