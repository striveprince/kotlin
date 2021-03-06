package com.lifecycle.binding.adapter

import androidx.annotation.IntDef
import com.lifecycle.binding.adapter.AdapterType.add
import com.lifecycle.binding.adapter.AdapterType.click
import com.lifecycle.binding.adapter.AdapterType.close
import com.lifecycle.binding.adapter.AdapterType.end
import com.lifecycle.binding.adapter.AdapterType.error
import com.lifecycle.binding.adapter.AdapterType.load
import com.lifecycle.binding.adapter.AdapterType.long
import com.lifecycle.binding.adapter.AdapterType.move
import com.lifecycle.binding.adapter.AdapterType.no
import com.lifecycle.binding.adapter.AdapterType.refresh
import com.lifecycle.binding.adapter.AdapterType.remove
import com.lifecycle.binding.adapter.AdapterType.select
import com.lifecycle.binding.adapter.AdapterType.set
import com.lifecycle.binding.adapter.AdapterType.start
import com.lifecycle.binding.adapter.AdapterType.success
import com.lifecycle.binding.adapter.AdapterType.touch

object AdapterType {
    const val no = 0x00
    const val load = 0x01
    const val add = 0x02
    const val refresh = 0x03
    const val remove = 0x04
    const val set = 0x05
    const val move = 0x06
    const val click = 0x07
    const val long = 0x08
    const val select = 0x09
    const val touch = 0x0a
    const val start = 0x0b
    const val end = 0x0c
    const val success = 0x0d
    const val error = 0x0e
    const val close = 0x0f
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [no, add,load, refresh, remove, set, move, click, long, select, touch,start,end,success, error,close])
annotation class AdapterEvent