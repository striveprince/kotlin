package com.lifecycle.binding.inter.inflate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Item {
    fun fragment(fm: FragmentManager, bundle: Bundle): Fragment
}