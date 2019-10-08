package com.customers.zktc.base.arouter

import android.app.Activity
import android.net.Uri
import android.os.Bundle

import com.alibaba.android.arouter.launcher.ARouter

class SchemaFilterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        if (uri != null) {
            ARouter.getInstance().build(uri).navigation()
        }
        finish()
    }
}