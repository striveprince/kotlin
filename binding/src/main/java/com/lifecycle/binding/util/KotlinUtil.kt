package com.lifecycle.binding.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/14 18:04
 * Email: 1033144294@qq.com
 */

fun<T> LiveData<T>.observer(owner: LifecycleOwner,block:(T)->Unit){
    observe(owner, Observer { block(it) })
}