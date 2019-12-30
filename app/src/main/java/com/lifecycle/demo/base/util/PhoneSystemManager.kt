package com.lifecycle.demo.base.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.FileUtils.filePathByUri
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw
import io.reactivex.Emitter
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 11:03
 * Email: 1033144294@qq.com
 */
class PhoneSystemManager : AppCompatActivity() {
    lateinit var photoFile: File

    companion object {
        const val type = "type"
        var emitter: Emitter<Any>? = null
        const val REQUEST_CAMERA = 0
        const val GPS_REQUEST_CODE = 10
        const val File_REQUEST_CODE = 1
        const val takePhoto = "takePhoto"
        const val permission = "permission"
        const val all = "*/*"
        const val image = "image/*"//选择图片
        const val audio = "audio/*" //选择音频
        const val video = "video/*" //选择视频 （mp4 3gp 是android支持的视频格式）
        const val video_image = "video/*;image/*"//同时选择视频和图片
        const val GPS = "gps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        when (val eventType = intent.getStringExtra(type)) {
            takePhoto -> takePhoto()
            GPS -> gpsSetting()
            permission -> openPermission()
            all, image, audio, video, video_image -> openFileManager(eventType)
        }
    }

    private fun openPermission() {
        try {
            val intent = when (Build.MANUFACTURER) {
                "HUAWEI" -> componentSettingIntent("com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity")
                "vivo" -> packageSettingIntent("com.bairenkeji.icaller")
                "OPPO" -> packageSettingIntent("com.coloros.safecenter")
                "Coolpad" -> packageSettingIntent("com.yulong.android.security:remote")
                "Xiaomi" -> miSettingIntent()
                "Meizu" -> meizuSettingIntent()
                "Sony" -> componentSettingIntent("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
                "LG" -> componentSettingIntent("com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity")
                else -> defaultIntent()
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            runCatching { startActivity(defaultIntent()) }
        }
    }

    private fun componentSettingIntent(pkg: String, clz: String) = Intent(packageName).apply { component = ComponentName(pkg, clz) }


    private fun meizuSettingIntent() =
        Intent("com.meizu.safe.security.SHOW_APPSEC").apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("packageName", packageName)
        }


    private fun miSettingIntent(): Intent {
        val rom: String = getMiuiVersion()
        return Intent().apply {
            when (rom) {
                "V6", "V7" -> {
                    action = "miui.intent.action.APP_PERM_EDITOR"
                    setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
                    putExtra("extra_pkgname", packageName)
                }
                "V8", "V9" -> {
                    action = "miui.intent.action.APP_PERM_EDITOR";
                    setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    putExtra("extra_pkgname", packageName);
                }
                else -> defaultIntent()
            }
        }
    }

    private fun getMiuiVersion(): String {
        val propName = "ro.miui.ui.version.name"
        val p = Runtime.getRuntime().exec("getprop $propName")
        return BufferedReader(InputStreamReader(p.inputStream), 1024).use { it.readLine() }
    }

    private fun Activity.packageSettingIntent(packageName: String): Intent {
        val packageInfo = packageManager.getPackageInfo(packageName, 0);
        return Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER)
                setPackage(packageInfo.packageName) }
            .let { packageManager.queryIntentActivities(it, 0).first() }
            .let { Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER)
                        component = ComponentName(it.activityInfo.packageName, it.activityInfo.name) } }
    }

    private fun defaultIntent(): Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", packageName, null) }

    private fun gpsSetting() {
        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST_CODE)
    }

    private fun openFileManager(type: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = type
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, File_REQUEST_CODE)
    }

    @Suppress("DEPRECATION")
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = File(Environment.getExternalStorageDirectory().absolutePath + tomtaw + System.currentTimeMillis() + ".jpg")
        photoFile.parentFile?.apply { mkdirs() }
        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".filterProvider", photoFile)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA -> emitter?.onNext(photoFile)
            File_REQUEST_CODE -> data?.data?.let { filePathByUri(it) }?.let { emitter?.onNext(File(it)) }
            GPS_REQUEST_CODE -> emitter?.onNext(true)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        emitter = null
    }
}