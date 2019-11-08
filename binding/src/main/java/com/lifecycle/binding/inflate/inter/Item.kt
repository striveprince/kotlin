package com.lifecycle.binding.inflate.inter

import android.view.ViewGroup

interface Item<T>{
    fun getItem(position: Int, container: ViewGroup): T
}