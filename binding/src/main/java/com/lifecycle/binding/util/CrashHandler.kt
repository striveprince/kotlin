package com.lifecycle.binding.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
/**
 * 保证只有一个 CrashHandler 实例
 */
@SuppressLint("SdCardPath")
class CrashHandler
private constructor(val context: Context) : Thread.UncaughtExceptionHandler {
    private val mDefaultHandler by lazy { Thread.getDefaultUncaughtExceptionHandler() }
    private val infos: MutableMap<String, String> = HashMap()
    private val packageName by lazy { context.packageName }
//    private val srcDirPath by lazy { (context.filesDir.toString() + File.separator + packageName.substring(packageName.lastIndexOf("\\.") + 1)) }

    private val srcDir by lazy { createPathDir(Environment.getExternalStorageDirectory(),packageName.replace(".", File.separator)) }
    private val timeName = SimpleDateFormat("MM_dd_HH_mm_ss_SSS", Locale.CHINA)
    private val fileName = String.format(Locale.getDefault(), "crash_%1s_error.log", timeName.format(Date()))
    private val dateDirs = SimpleDateFormat("yyyyMM/dd", Locale.CHINA)

    companion object {
        private var instance: CrashHandler? = null
        fun getInstance(context: Context): CrashHandler {
            return instance ?: synchronized(CrashHandler::class.java) { instance ?: CrashHandler(context) }
        }
    }

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
//            runCatching { Thread.sleep(1000) }
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }


    /**
     * 保存错误信息到文件中 *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfoInFile(ex: Throwable): String {
        val sb = StringBuilder()
        val writer = StringWriter()
        PrintWriter(writer).use {
            var cause = ex.cause
            while (cause != null) {
                cause.printStackTrace(it)
                cause = cause.cause
            }
        }
        sb.append(writer.toString())
        return runCatching {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                File(srcDir,fileName).let {
                    if(it.isFile || it.createNewFile()) FileOutputStream(it).use { it.write(sb.toString().toByteArray()) }
                }
            }
            fileName
        }.getOrElse {
            Timber.e(it)
            ""
        }
    }

    /**
     * 整理异常信息
     *
     * @param e
     * @return
     */
    fun getTraceInfo(e: Throwable): StringBuffer {
        val sb = StringBuffer()
        val ex = (if (e.cause == null) e else e.cause!!)
        val stacks = ex.stackTrace
        sb.append(collectDeviceInfo(context, false))
        for (i in stacks.indices) {
            sb.append(ex.toString())
                .append("\n")
        }
        Timber.e(sb.toString())
        return sb
    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private fun collectDeviceInfo(ctx: Context, flag: Boolean): String {
        try {
            val pi = ctx.packageManager.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("an error occured when collect package info ${e.message}")
            infos["versionName"] = "unknow"
            infos["versionCode"] = "unknow"
        }
        val stringBuffer = StringBuffer("""versionName:${infos["versionName"]}, versionCode:${infos["versionCode"]}""".trimIndent())
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            runCatching {
                field.isAccessible = true
                infos[field.name] = field[null].toString()
                Timber.i("${field.name}:${field[null]}")
                if (flag) stringBuffer.append("${field.name}: ${infos[field.name]}".trimIndent())
            }.getOrElse { Timber.e(it) }
        }
        return stringBuffer.toString()
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private fun handleException(ex: Throwable): Boolean {
        ex.printStackTrace()
        saveCrashInfoInFile(ex)
        return true
    }

}