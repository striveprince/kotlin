package com.lifecycle.binding.inter.inflate

import com.lifecycle.binding.inter.Select

open class ViewSelectInflate:ViewBindInflate(),Select {
    override var checkWay: Int = 3
    override var isChecked: Boolean = false
}