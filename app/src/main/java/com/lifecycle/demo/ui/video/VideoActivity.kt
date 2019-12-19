package com.lifecycle.demo.ui.video

import android.Manifest
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.binding.DataBindingActivity
import com.lifecycle.demo.base.util.rxPermissionsDialog
import com.lifecycle.demo.databinding.ActivityVideoBinding
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.demo.ui.video.VideoActivity.Companion.video
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.util.subscribeObserver

@Route(path = video)
@LayoutView(layout = [R.layout.activity_video])
class VideoActivity : DataBindingActivity<VideoModel, ActivityVideoBinding>() {
    companion object { const val video = ActivityComponent.Config.tomtaw + "video" }
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermissionsDialog(*permissions)
            .subscribeObserver(onError = { finish() }) { initYealinkApi() }
    }

    private fun initYealinkApi() {

    }
}