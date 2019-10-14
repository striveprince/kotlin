package com.binding.model.base.spannable

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/14 11:03
 * Email: 1033144294@qq.com
 */
class SpannableUtil(textView: TextView) {
//    private val spannableStringBuilder = SpannableStringBuilder()

    init {
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private val spannableTexts = ArrayList<SpannableText>()

    fun addClick(text:String,color:String="",click: (View)->Unit,updateDrawState:(TextPaint)->Unit={}):SpannableUtil{
        if (TextUtils.isEmpty(color)){
            spannableTexts.add(SpannableText(text,0,true, click,updateDrawState,false))
        }else{
            spannableTexts.add(SpannableText(text,Color.parseColor(color),true, click,updateDrawState,true))
        }
        return this
    }

    fun addColor(text:String,color:Int): SpannableUtil {
        spannableTexts.add(SpannableText(text,color,bColor = true))
        return this
    }

    fun addClick(text:String,color:Int,click: (View)->Unit,updateDrawState:(TextPaint)->Unit={}):SpannableUtil{
        spannableTexts.add(SpannableText(text,color,true, click,updateDrawState,true))
        return this
    }

    fun addText(s: String): SpannableUtil {
        spannableTexts.add(SpannableText(s))
        return this
    }

    fun build(){
        init(spannableTexts)
    }

    private fun init(es: List<SpannableText>): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        for (text in es) builder.append(text.name)
        builder.clearSpans()
        builder.append(builder)
        var index = 0
        for (e in es) {
//            if (e.bColor) {
//                val colorSpan = ForegroundColorSpan(e.color)
//                builder.setSpan(colorSpan, index, index + e.name.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
//            }
            if (e.enableClick()) {
                builder.setSpan(e, index, index + e.name.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            index += e.name.length
        }
        return builder
    }

}