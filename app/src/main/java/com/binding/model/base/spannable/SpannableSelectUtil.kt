package com.binding.model.base.spannable

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView

import com.binding.model.adapter.IEventAdapter

import java.util.ArrayList

import com.binding.model.inflate.obj.EventType.select


class SpannableSelectUtil<E : SpannableSelectRecycler<*>>(
    textView: TextView,
    private val format: String,
    private val max: Int
) : IEventAdapter<E> {
    private val checkList = ArrayList<E>()
    private val es = ArrayList<E>()
    private val iEventAdapter = this
    private val spannableStringBuilder = SpannableStringBuilder()

    private var eventAdapter: IEventAdapter<E> = this
    private var interpolator:((E)->Boolean) = { false }


    init {
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun init(list: List<E>, check: List<E> = ArrayList()) {
        es.clear()
        checkList.clear()
        es.addAll(list)
        checkList.addAll(if (check.size > max) check.subList(0, max) else check)
        init()
    }


    fun init() {
        val builder = StringBuilder()
        for (text in es) {
            text.iEventAdapter = iEventAdapter
            builder.append(text.name()).append(format)
        }
        builder.delete(builder.lastIndexOf(format), builder.length)
        spannableStringBuilder.clear()
        spannableStringBuilder.clearSpans()
        spannableStringBuilder.append(builder)
        var index = 0
        for (e in es) {
            val span = e.getClickableSpan(checkList.contains(e))
            spannableStringBuilder.setSpan(span, index, index + e.name().length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            index += e.name().length+format.length
        }
    }

    fun remove(position: Int) {
        val e = es[position]
        remove(e)
    }

    fun remove(e: E) {
        es.remove(e)
        checkList.remove(e)
        init()
    }

    fun add(e: E) {
        es.add(if (es.size - 2 > 0) es.size - 2 else 0, e)
        init()
    }


    override fun setEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        when (type) {
            select -> if (!interpolator.invoke(e) && select(e, !checkList.contains(e), isPush(e))) init()
            else -> eventAdapter.setEntity(position, e, type, view)
        }
        return false
    }

    /**
     * checkway
     * 0       1       2       3
     * push     false   true   false    true
     * takeBack false   false  true     true
     */
    private fun isPush(e: E): Boolean {
        val status = e.checkWay() and 1
        return status == 1
    }

    private fun isTakeBack(e: E): Boolean {
        val status = e.checkWay() shr 1 and 1
        return status == 1
    }

    private fun select(inE: E, check: Boolean, push: Boolean): Boolean {
        var success = true
        if (!check) {
            if (!isTakeBack(inE)) {
                inE.check(true)
            } else {
                checkList.remove(inE)
            }
        } else {
            if (push) {
                inE.check(true)
                checkList.add(inE)
                while (checkList.size > max) {
                    val out = checkList.removeAt(0)
                    out.check(false)
                }
            } else {
                success = addCheck(inE)
                inE.check(success)
            }
        }
        return success
    }

    private fun addCheck(inE: E): Boolean {
        val success = checkList.size < max
        return success && checkList.add(inE)
    }


    fun setIEventAdapter(iEventAdapter: IEventAdapter<E>) {
        this.eventAdapter = iEventAdapter
    }

    fun setInterpolator(interpolator: ((E)->Boolean)) {
        this.interpolator = interpolator
    }

    fun clear() {
        eventAdapter = this
        checkList.clear()
        es.clear()
        spannableStringBuilder.clear()
        spannableStringBuilder.clearSpans()
    }
}
