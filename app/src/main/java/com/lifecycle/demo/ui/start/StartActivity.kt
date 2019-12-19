package com.lifecycle.demo.ui.start

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.anko.AnkoActivity
import com.lifecycle.demo.base.life.viewmodel.BaseViewModel
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.base.util.checkPermissions
import com.lifecycle.demo.base.util.rxPermissions
import com.lifecycle.demo.inject.component.ActivityComponent.Config.tomtaw
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.ui.start.StartActivity.Companion.start
import com.lifecycle.binding.base.rotate.TimeUtil
import com.lifecycle.binding.util.subscribeObserver
import org.jetbrains.anko.*

@Route(path = start)
class StartActivity : AnkoActivity<BaseViewModel>() {
    companion object {
        const val start = tomtaw + "start"
    }

    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(api, owner, bundle)
        if (!checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
            rxPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribeObserver(onError = { finish() }) {
                    start()
                }
        }else start()
    }

    private fun start(){
        ARouterUtil.start()
        TimeUtil.handler.postDelayed({ finish() }, 500)
    }

    override fun parse(t: BaseViewModel, context: Context): AnkoContext<Context> {
        return AnkoContext.create(this).apply {
            frameLayout {
                imageView {
                    imageResource = R.mipmap.ic_launcher
                }.lparams(matchParent, matchParent)
                lparams(matchParent, matchParent)
            }
        }
    }

}