package com.lifecycle.binding.inter

import android.view.ViewGroup

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 16:26
 * Email: 1033144294@qq.com
 */
interface Item<T>{
    fun getItem(position: Int, container: ViewGroup): T
}