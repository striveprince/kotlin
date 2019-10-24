package com.binding.model

import com.binding.model.base.ListMap
import com.binding.model.inflate.inter.Parse
import com.customers.zktc.BR


object Config {
    const val title="title"
    const val bundle="bundle"
    const val path = "path"
    val vm = BR.vm
    val array = ListMap<String, Parse<*>>()



}