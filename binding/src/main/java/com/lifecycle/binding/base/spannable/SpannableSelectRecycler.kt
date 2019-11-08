package com.lifecycle.binding.base.spannable

import android.text.style.ClickableSpan

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inflate.inter.Check

import com.lifecycle.binding.inflate.inter.Inflate
import com.lifecycle.binding.inflate.inter.Recycler

interface SpannableSelectRecycler<Binding : ViewDataBinding> : Check<Binding> {
    fun name(): String
    fun getClickableSpan(contains: Boolean): ClickableSpan
}
