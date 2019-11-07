package com.customers.zktc.base.adapter

import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.inter.Inflate

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 11:50
 * Email: 1033144294@qq.com
 */
interface Filter <Binding:ViewDataBinding>:Inflate<Binding> {
    fun compare(text:String):Boolean
}