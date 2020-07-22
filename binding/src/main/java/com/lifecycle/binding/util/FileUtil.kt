package com.lifecycle.binding.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStream


/**
 *
 * @ProjectName:    kotlin
 * @Package:        com.lifecycle.binding.util
 * @ClassName:      FileUtil
 * @Description:
 * @Author:         A
 * @CreateDate:     2020/3/31 13:50
 * @UpdateUser:     A
 * @UpdateDate:     2020/3/31 13:50
 * @UpdateRemark:
 * @Version:
 */

fun createPathDir(srcDirPath: File, path: String): File {
    return File(srcDirPath, path).run {
        if (isDirectory || mkdirs()) this
        else srcDirPath
    }
}

fun assertToString(context: Context, fileName:String):String{
    val applicationContext = if(context is Application) context else context.applicationContext
    val stringBuilder = StringBuilder()
    BufferedReader(InputStreamReader(applicationContext.assets.open(fileName))).useLines { it ->
        it.forEach { stringBuilder.append(it) }
    }
    return stringBuilder.toString()
}

fun assertToFile(context: Context, from: String, targetFile: File) :Boolean{
    return createDirFile(targetFile, context.assets, from)
}

fun createDirFile(file: File, assets: AssetManager, from: String, path: String = from): Boolean {
    return runCatching {
        assets.list(path)?.let {
            File(file, from).run {
                if(it.isNotEmpty()){
                    var success = createDir()
                    Timber.i("mkdir: $success absolutePath = $absolutePath;path = $path")
                    for (s in it) success = createDirFile(File(file, from), assets, s, path + File.separatorChar + s)
                    success
                }else {
                    createNewFile().apply {
                        Timber.i("createFile: $this absolutePath = $absolutePath;path = $path")
                        assets.open(path).runCatching {
                            val buffer = ByteArray(1024)
                            val outPutStream = outputStream()
                            var n = 0
                            while (-1 != (read(buffer).also { n = it })) outPutStream.write(buffer, 0, n)
                            close()
                            Timber.i("createFile: length = ${length()};path = $path")
                        }
                    }
                }
            }
        } ?: false
    }.getOrElse { false }
}


fun Context.realPathFromURI(contentUri: Uri): String? {
    val pro = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(contentUri, pro, null, null, null)
    return cursor?.use {
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.getString(columnIndex)
        } else null
    }
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}


private fun Context.getDataColumn(uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)
    return contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
        if (it.moveToFirst()) it.getString(it.getColumnIndexOrThrow(column))
        else null
    }
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

@TargetApi(19)
fun Activity.getImageAbsolutePath(uri: Uri): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
        return isDocumentUri(uri)
    } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {// MediaStore (and general)
        if (isGooglePhotosUri(uri)) uri.lastPathSegment// Return the remote address
        else getDataColumn(uri, null, null)
    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
        uri.path
    } else null
}

fun Activity.filePathByUri(uri: Uri): String? {
    return if (ContentResolver.SCHEME_FILE == uri.scheme) uri.path  // 以 file:// 开头的
    else if (ContentResolver.SCHEME_CONTENT == uri.scheme && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (index > -1) it.getString(index) else null
            } else null
        }
    } else
    // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT == uri.scheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            return isDocumentUri(uri)
        } else realPathFromURI(uri)
            .apply { if (TextUtils.isEmpty(this)) getImageAbsolutePath(uri) }
}

@TargetApi(19)
fun Activity.isDocumentUri(uri: Uri): String? {
    return if (isExternalStorageDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if ("primary".equals(split[0], ignoreCase = true)) return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
        else null
    } else if (isDownloadsDocument(uri)) {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id.toLong())
        getDataColumn(contentUri, null, null)
    } else if (isMediaDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val contentUri: Uri = when (split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> Uri.EMPTY
        }
        val selection = MediaStore.Images.Media._ID + "=?"
        getDataColumn(contentUri, selection, arrayOf(split[1]))
    } else null
}

val srcFileDir = Environment.getExternalStorageDirectory().toString() + "/111"

fun createWholeDir(path: String): String {
    var path1 = path
    val builder = StringBuilder()
    builder.append(srcFileDir)
    if (path1.startsWith(srcFileDir)) {
        path1 = path1.replace(srcFileDir + File.separatorChar, "")
    }
    val dirs = path1.split(File.separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    for (dir in dirs) {
        builder.append(File.separatorChar)
        builder.append(dir)
        if (File(builder.toString()).createDir()) {
            return ""
        }
    }
    return builder.toString()
}

fun createDir(path: String): Boolean {
    return File(path.run { if (endsWith(File.separatorChar)) substring(0, length - 1) else this }).createDir()
}

fun File.createDir(): Boolean {
    return isDirectory || (exists() && delete()) && mkdirs() ||mkdirs()
}
