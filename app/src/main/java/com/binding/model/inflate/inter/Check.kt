package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/23 14:35
 * Email: 1033144294@qq.com
 */
interface Check<Binding:ViewDataBinding> :Inflate<Binding>{
    fun check(check: Boolean)
    fun getCheckType(): Int
    /**
     * 0       1       2       3
     * push     false   true   false    true
     * takeBack false   false  true     true
     */
    fun checkWay(): Int
}