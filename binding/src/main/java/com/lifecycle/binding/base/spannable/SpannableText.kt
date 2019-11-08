package com.lifecycle.binding.base.spannable

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/14 11:00
 * Email: 1033144294@qq.com
 */
class SpannableText(
    val name:CharSequence,
    val color: Int = 0,
    private val enable:Boolean = false,
    private val click:(View)->Unit = {},
    private val updateDrawState:(TextPaint)->Unit = {},
    val bColor: Boolean = false
): ClickableSpan() {

    fun enableClick():Boolean = enable

    override fun onClick(widget: View) {
        click(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        updateDrawState.invoke(ds)
    }


}