package com.binding.model.base.spannable

import android.text.style.ClickableSpan

import androidx.databinding.ViewDataBinding

import com.binding.model.inflate.inter.Inflate
import com.binding.model.inflate.inter.Recycler

interface SpannableSelectRecycler<T : ViewDataBinding> : Recycler<T> {
    fun name(): String
    fun getClickableSpan(contains: Boolean): ClickableSpan
}
