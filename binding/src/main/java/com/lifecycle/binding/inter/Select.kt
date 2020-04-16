package com.lifecycle.binding.inter

import com.lifecycle.binding.inter.inflate.Inflate

interface Select:Inflate {

    /**
     *          0       1       2       3
     * push     false   true   false    true
     * takeBack false   false  true     true
     * */
    var checkWay: Int
    fun select(b: Boolean): Boolean {
        return b
    }

    fun isSelected():Boolean = false

}
