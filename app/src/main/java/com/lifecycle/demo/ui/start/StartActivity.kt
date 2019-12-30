package com.lifecycle.demo.ui.start

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.base.rotate.TimeUtil
import com.lifecycle.binding.life.binding.BindingActivity
import com.lifecycle.binding.util.subscribeObserver
import com.lifecycle.binding.rx.viewmodel.RxLifeViewModel
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.base.util.checkPermissions
import com.lifecycle.demo.base.util.rxPermissions
import com.lifecycle.demo.databinding.ActivityStartBinding
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw
import com.lifecycle.demo.ui.start.StartActivity.Companion.start

@Route(path = start)
class StartActivity : BindingActivity<RxLifeViewModel,ActivityStartBinding>() {
    companion object {
        const val start = tomtaw + "start"
    }

    private fun start(){
        ARouterUtil.home()
        TimeUtil.handler.postDelayed({ finish() }, 500)
    }
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        if (!checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
            rxPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribeObserver(onError = { finish() }) {
                    start()
                }
        }else start()
    }



}