package com.lifecycle.binding.inter.inflate

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Item {
    fun fragment(fm: FragmentManager): Fragment
}