package com.lifecycle.binding.adapter

import androidx.annotation.IntDef
import com.lifecycle.binding.adapter.AdapterType.add
import com.lifecycle.binding.adapter.AdapterType.click
import com.lifecycle.binding.adapter.AdapterType.load
import com.lifecycle.binding.adapter.AdapterType.long
import com.lifecycle.binding.adapter.AdapterType.move
import com.lifecycle.binding.adapter.AdapterType.no
import com.lifecycle.binding.adapter.AdapterType.refresh
import com.lifecycle.binding.adapter.AdapterType.remove
import com.lifecycle.binding.adapter.AdapterType.select
import com.lifecycle.binding.adapter.AdapterType.set
import com.lifecycle.binding.adapter.AdapterType.touch

object AdapterType {
    const val no = 0
    const val load = 1
    const val add = 2
    const val refresh = 3
    const val remove = 4
    const val set = 5
    const val move = 6
    const val click = 7
    const val long = 8
    const val select = 9
    const val touch = 10
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [no, add,load, refresh, remove, set, move, click, long, select, touch])
annotation class AdapterEvent