package com.customers.zktc.base.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.customers.zktc.BuildConfig
import com.customers.zktc.R
import com.customers.zktc.inject.component.ActivityComponent
import io.reactivex.Emitter
import java.io.File

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/11 11:03
 * Email: 1033144294@qq.com
 */
class PhoneSystemManager : AppCompatActivity() {

    companion object {
        const val type = "type"
        var emitter: Emitter<Any>? = null
        const val REQUEST_CAMERA = 0
        const val GPS_REQUEST_CODE = 10
        const val none = "none"
        const val all = "*/*"
        const val image = "image/*"//选择图片
        const val audio = "audio/*" //选择音频
        const val video = "video/*" //选择视频 （mp4 3gp 是android支持的视频格式）
        const val video_image = "video/*;image/*"//同时选择视频和图片
        const val setting_gps = "setting_gps"
    }
    var photoFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val eventType = intent.getStringExtra(type)
        if (none == eventType) {
            takePhoto()
        } else if(setting_gps == eventType) {
            openGpsSetting()
        }else{
            openFileManager(eventType)
        }
    }

    private fun openGpsSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, GPS_REQUEST_CODE)
    }


    private fun openFileManager(type: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = type
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 1)
    }

    private fun takePhoto() {
        photoFile = File(Environment.getExternalStorageDirectory().toString() + ActivityComponent.Config.zktc + System.currentTimeMillis() + ".jpg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (!photoFile!!.parentFile.exists()) photoFile!!.parentFile.mkdirs()
        val uri =FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".filterProvider", photoFile!!)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            photoFile?.let { emitter?.onNext(it) }
        } else if (resultCode == Activity.RESULT_OK) {
            emitter?.onNext(File(data?.data?.let { FileUtils.getFilePathByUri(this, it) }))
        }else if(requestCode== GPS_REQUEST_CODE){
            emitter?.onNext(checkPermissions(this,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        photoFile = null
        emitter = null
    }
}