package com.lifecycle.binding.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.lifecycle.binding.inter.observer.NormalObserver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.base.bus.RxBus
import com.lifecycle.binding.base.rotate.TimeUtil
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/14 18:04
 * Email: 1033144294@qq.com
 */


inline fun <reified E> rxBus(): Observable<E> {
    return RxBus.getInstance()
        .toObservable(E::class.java)
}

inline fun<reified E> rxBusMain():Observable<E>{
    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
}

fun busPost(any: Any) {
    RxBus.getInstance().send(any)
}
fun Context.string(@StringRes id: Int, vararg any: Any) =
    getString(id, *any)

fun Context.drawable(@DrawableRes id:Int)
        = ContextCompat.getDrawable(this,id)

fun Context.color(@ColorRes id:Int)
        = ContextCompat.getColor(this,id)

fun Context.floatToPx(float: Float)= resources.displayMetrics.density*float
fun Context.floatToDp(float: Float)= float/resources.displayMetrics.density
fun Context.screenWidth()= resources.displayMetrics.widthPixels
fun Context.screenHeight()= resources.displayMetrics.heightPixels

fun<T> LiveData<T>.observer(owner: LifecycleOwner,block:(T)->Unit){
    observe(owner, Observer { block(it) })
}

fun findLayoutView(thisCls: Class<*>): LayoutView {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return thisCls.getAnnotation(LayoutView::class.java)
        ?: findLayoutView(thisCls = thisCls.superclass!!)
}


val gson = Gson()

inline fun <reified T> toArray(list: List<T>): Array<T> {
    return ArrayList<T>(list).toArray(arrayOf())
}

@ImplicitReflectionSerializer
inline fun <reified T : Any> parse(string: String): T {
    return Json.parse(string)
}

fun <T, R> Observable<List<T>>.concatIterable(block: T.() -> R): Observable<R> =
    this.concatMapIterable {it}
        .map { block(it) }

fun <T, R> Observable<List<T>>.concatList(block: T.() -> R): Observable<List<R>> =
    this.concatIterable(block)
        .toList()
        .toObservable()


inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)!!

inline fun <reified T> String.fromJson() = gson.fromJson<T>(this)

fun contain(value: Int, min: Int, max: Int): Boolean {
    return value in min until max
}

fun containsList(value: Int, list: List<*>): Boolean {
    return contain(value, 0, list.size)
}

inline fun <T, R> T.transform(block: T.() -> R): R {
    return block()
}


//---- file -----
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
        if (createDir(File(builder.toString())) == FAILED) {
            return ""
        }
    }
    return builder.toString()
}

fun <T> Observable<T>.subscribeNormal(

    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {},
    onNext: (T) -> Unit = {}
):Disposable{
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
    return observer.disposable.get()
}

fun <T> Observable<T>.subscribeObserver(
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {},
    onNext: (T) -> Unit = {}
){
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
}


//-------------Single---------------

fun <T> Single<T>.subscribeNormal(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) :Disposable{
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    this.subscribe(observer)
    return observer.disposable.get()
}


fun <T> Single<T>.subscribeObserver(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) {
    subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
}


fun <T> Flowable<T>.subscribeObserver(
    onSubscribe: (Disposable) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
) {

    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
    onSubscribe.invoke(disposable)
}


fun <T> Flowable<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
):Disposable {
    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
    onSubscribe.invoke(disposable)
    return disposable
}

fun toast(e: Throwable) {
    if(!TextUtils.isEmpty(e.message))
        toast(e.message!!)
}


fun toast(message: String?) {
    if(message?.trim()?.isNotEmpty() == true)
        Toast.makeText(AppLifecycle.activity(), message, Toast.LENGTH_SHORT).show()
}

fun installApkFile(
    context: Context,
    file: File,
    fileProvider: String = "com.lifecycle.demo.fileProvider"
) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val apkUri = FileProvider.getUriForFile(context, fileProvider, file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
    }
    context.startActivity(intent)
}

const val SUCCESS = 1
const val FAILED = 0
fun createDir(path: String): Int {
    var p = path
    val l = p.length
    if (p[l - 1] == File.separatorChar) { //如果末尾是 /
        p = path.substring(0, l - 1)
    }
    return createDir(File(p))
}

fun createDir(file: File): Int {
    if (file.exists()) {
        if (file.isDirectory)
            return SUCCESS
        if (!file.delete()) return FAILED// 避免他是一个文件存在
    }
    return if (file.mkdirs()) SUCCESS else FAILED
}


/**
 * 小米
 *
 * @param activity
 * @param darkmode true为深色
 * @return
 */
@SuppressLint("PrivateApi")
fun setMiuiStatusBarDarkMode(activity: Activity, darkmode: Boolean): Boolean {
    val clazz = activity.window.javaClass
    try {

        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        var darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod(
            "setExtraFlags",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
        return true
    } catch (e: Exception) {
    }

    return false
}

/**
 * Meizu的
 *
 * @param activity
 * @param dark     true为深色
 * @return
 */
fun setMeizuStatusBarDarkIcon(activity: Activity?, dark: Boolean): Boolean {
    var result = false
    if (activity != null) {
        try {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        } catch (e: Exception) {
        }

    }
    return result
}

/**
 * 6.0以上系统，可以设置为深色字体
 *
 * @param activity
 */
fun setAllUpSixVersion(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun <T> Single<T>.ioToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.newToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun post(delayMillis:Long=0, block: () -> Unit){
    if(delayMillis<=0)TimeUtil.handler.post(Runnable(block))
    else TimeUtil.handler.postDelayed(Runnable(block),delayMillis)
}

