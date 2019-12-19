package com.lifecycle.binding.adapter.databinding.inter

import android.text.Editable

interface AfterTextChangedError {
    fun checkError(s: Editable): CharSequence?
}