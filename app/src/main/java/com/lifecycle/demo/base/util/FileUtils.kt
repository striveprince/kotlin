@file:Suppress("DEPRECATION")

package com.lifecycle.demo.base.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 12:00
 * Email: 1033144294@qq.com
 */
object FileUtils {

    @JvmStatic
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


    private fun Context.getDataColumn( uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
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
    @JvmStatic
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    @TargetApi(19)
    fun Activity.getImageAbsolutePath( uri: Uri): String? {
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
                .apply { if (TextUtils.isEmpty(this)) getImageAbsolutePath( uri) }
    }

    @TargetApi(19)
    fun Activity.isDocumentUri(uri: Uri): String? {
        return  if (isExternalStorageDocument(uri)) {
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
}