package com.customers.zktc.ui.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:23
 * Email: 1033144294@qq.com
 */
class DemoModel : ViewModel() {
    val name = MutableLiveData<String>("click me")

    fun click() {
        name.value = if (name.value === "click") "click me" else "click"
    }
}