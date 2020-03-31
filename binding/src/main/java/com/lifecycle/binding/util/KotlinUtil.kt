package com.lifecycle.binding.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.rotate.TimeUtil
import com.lifecycle.binding.inter.bind.annotation.LayoutView
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


/**
 *  error   run     state
 *  FF FF   FF      FF
 */

fun isStateRunning(@AdapterEvent state :Int):Boolean{
    return state.shr(8) and 1 == 1 && state .shr(9) and 1 == 0
}

fun stateStart(@AdapterEvent state :Int):Int{
    return state or 0x300
}

fun stateEnd(@AdapterEvent state :Int):Int{
    return state and 0x100FF
}


fun stateError(state :Int):Int{
    return state or 0x10000
}

fun stateSuccess(state :Int):Int{
    return state and 0x1ff
}

fun stateOriginal(state: Int):Int{
    return state and 0xff
}

fun Int.stateEqual(@AdapterEvent state: Int):Boolean{
    return (this and 0xff) == state
}

val gson = Gson()

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)!!

inline fun <reified T> String.fromJson() = gson.fromJson<T>(this)

fun <T> T?.toJson():String{
    return this?.let { gson.toJson(it) }?:""
}

fun <T,R> Collection<T>.converter(block: (T) -> R):Set<R>{
    val set = HashSet<R>()
    for (t in this) set.add(block(t))
    return set
}

fun Context.application():Context{
    return if(this is Application)this else applicationContext
}

fun Context.sharedPreferences(name:String):SharedPreferences{
    return application().getSharedPreferences(name,Activity.MODE_PRIVATE)
}

//inline fun <reified E> rxBus(): Observable<E> {
//    return RxBus.getInstance()
//        .toObservable(E::class.java)
//}
//inline fun<reified E> rxBusMain():Observable<E>{
//    return rxBus<E>().observeOn(AndroidSchedulers.mainThread())
//}

//fun busPost(any: Any) {
//    RxBus.getInstance().send(any)
//}
fun Context.string(@StringRes id: Int, vararg any: Any) =
    getString(id, *any)

fun Context.drawable(@DrawableRes id:Int) = ContextCompat.getDrawable(this,id)
fun Context.color(@ColorRes id:Int) = ContextCompat.getColor(this,id)

val density by lazy { AppLifecycle.application.resources.displayMetrics.density }
val screenWidth by lazy { AppLifecycle.application.resources.displayMetrics.widthPixels }
val screenHeight by lazy { AppLifecycle.application.resources.displayMetrics.heightPixels }
fun dip(int: Int) = (density*int).toInt()
fun pxToDip(int: Int) = (int/density+0.5).toInt()

fun<T> LiveData<T>.observer(owner: LifecycleOwner,block:(T)->Unit){
    observe(owner, Observer { block(it) })
}

fun findLayoutView(thisCls: Class<*>): LayoutView {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return thisCls.getAnnotation(LayoutView::class.java)
        ?: findLayoutView(thisCls = thisCls.superclass!!)
}



fun <T,R> List<T>.converter(block: T.() -> R):List<R>{
    val list = ArrayList<R>()
    for (t in this) list.add( t.block())
    return list
}




inline fun <reified T> toArray(list: List<T>): Array<T> {
    return ArrayList<T>(list).toArray(arrayOf())
}

@ImplicitReflectionSerializer
inline fun <reified T : Any> parse(string: String): T {
    return Json.parse(string)
}


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

//fun <T> Observable<T>.subscribeNormal(
//
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onSubscribe: (Disposable) -> Unit = {},
//    onNext: (T) -> Unit = {}
//):Disposable{
//    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
//    this.subscribe(observer)
//    return observer.disposable.get()
//}

//fun <T> Observable<T>.subscribeObserver(
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onSubscribe: (Disposable) -> Unit = {},
//    onNext: (T) -> Unit = {}
//){
//    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
//    this.subscribe(observer)
//}


//-------------Single---------------
//
//fun <T> Single<T>.subscribeNormal(
//    onSubscribe: (Disposable) -> Unit = {},
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onNext: (T) -> Unit = {}
//) :Disposable{
//    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
//    this.subscribe(observer)
//    return observer.disposable.get()
//}

//
//fun <T> Single<T>.subscribeObserver(
//    onSubscribe: (Disposable) -> Unit = {},
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onNext: (T) -> Unit = {}
//) {
//    subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
//}
//
//
//fun <T> Flowable<T>.subscribeObserver(
//    onSubscribe: (Disposable) -> Unit = {},
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onNext: (T) -> Unit = {}
//) {
//
//    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
//    onSubscribe.invoke(disposable)
//}
//
//
//fun <T> Flowable<T>.subscribeNormal(
//    onNext: (T) -> Unit = {},
//    onError: (Throwable) -> Unit = { toast(it) },
//    onComplete: () -> Unit = {},
//    onSubscribe: (Disposable) -> Unit = {}
//):Disposable {
//    val disposable = subscribe(Consumer(onNext), Consumer(onError), Action(onComplete))
//    onSubscribe.invoke(disposable)
//    return disposable
//}

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

fun post(delayMillis:Long=0, block: () -> Unit){
    if(delayMillis<=0)TimeUtil.handler.post(Runnable(block))
    else TimeUtil.handler.postDelayed(Runnable(block),delayMillis)
}

