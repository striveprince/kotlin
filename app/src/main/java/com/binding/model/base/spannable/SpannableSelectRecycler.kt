package com.binding.model.base.spannable

import android.text.style.ClickableSpan

import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.inter.Check

import com.binding.model.inflate.inter.Inflate
import com.binding.model.inflate.inter.Recycler

interface SpannableSelectRecycler<Binding : ViewDataBinding> : Check<Binding> {
    fun name(): String
    fun getClickableSpan(contains: Boolean): ClickableSpan
}
