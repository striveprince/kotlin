package com.lifecycle.binding.base

import android.text.TextUtils

import java.util.Locale

/**
 * Created by pc on 2017/9/28.
 */

class Text(
    private val text: String,
    private val color: String = "",
    private val big: Int = 0,
    private val line: Boolean = false) {

    constructor(text: String,color: Int,big: Int=0,line: Boolean=false) :
            this(text,color = String.format("%1x", color and 0x00FFFFFF),big = big,line = line)

    override fun toString(): String {
        val html: String
        if (TextUtils.isEmpty(color) && big == 0)
            html = text
        else if (big == 0) {
            html = String.format(Locale.getDefault(), "<font color = '#%1s'>%2s</font>", color, text)
        } else if (TextUtils.isEmpty(color)) {
            val builder = StringBuilder()
            for (i in 0 until big) builder.append("<big>")
            builder.append("%1s")
            for (i in 0 until big) builder.append("</big>")
            html = String.format(Locale.getDefault(), builder.toString(), text)
        } else {
            val builder = StringBuilder()
            builder.append("<font color = '#%1s'>")
            for (i in 0 until big) builder.append("<big>")
            builder.append("%2s")
            for (i in 0 until big) builder.append("</big>")
            builder.append("</font>")
            html = String.format(Locale.getDefault(), builder.toString(), color, text)
        }
        return if (line) "$html<br>" else html
    }
}
