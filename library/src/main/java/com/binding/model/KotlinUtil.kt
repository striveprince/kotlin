package com.binding.model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.binding.model.annoation.LayoutView
import com.binding.model.base.RxBus
import com.binding.model.base.Text
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.model.ViewModel
import com.binding.model.inflate.observer.NormalObserver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File


const val pageWay = false
val gson = Gson()

fun findModelView(thisCls: Class<*>): LayoutView {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return thisCls.getAnnotation(LayoutView::class.java)
        ?: return findModelView(thisCls = thisCls.superclass)
}

inline fun <reified T> toArray(list: List<T>): Array<T> {
    return ArrayList<T>(list).toArray(arrayOf())
}


//
//
//@ImplicitReflectionSerializer
//inline fun <reified T:Any> String.json():T{
//    val j = Json(JsonConfiguration.Default)
//    val v = j.parseJson(this)
//    return j.fromJson(v)
//}
//inline fun <reified T> String.json():T{
//    Moshi.Builder().add(KotlinJsonAdapterFactory())
//}


//inline fun <reified E> rxBus(owner: LifecycleOwner): Observable<E> {
//    val provider = AndroidLifecycle.createLifecycleProvider(owner)
//    return rxBus<E>()
//        .compose(provider.bindToLifecycle<E>())
//}

inline fun <reified E> rxBus(): Observable<E> {
    return RxBus.getInstance()
        .toObservable(E::class.java)
}

fun busPost(any: Any) {
    return RxBus.getInstance().send(any)
}


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
val srcFileDir = Environment.getExternalStorageDirectory().toString() + "/zktc"

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

//-------------Observable---------------
//inline fun <reified T> Observable<T>.merge(observable: Observable<T>){
//    this.mergeWith(observable)
//}

fun <T> Observable<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    this.subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
}

fun <T> Observable<T>.subscribeNormal(
    t: CycleContainer<*>,
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    t.cycle.addObserver(observer)
    this.subscribe(observer)
}

fun <T : Any> Observable<T>.subscribeNormal(
    t: ViewModel<*, *>,
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    t.t.cycle.addObserver(observer)
    if (t.disposable == null || t.disposable?.isDisposed!!) {
        this.subscribe(observer)
        t.disposable = observer.disposable.get()
    }
}

//-------------Flowable---------------
fun <T> Flowable<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    onSubscribe.invoke(subscribe(Consumer(onNext), Consumer(onError), Action(onComplete)))
}

//-------------Single---------------
fun <T> Single<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    this.subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
}

fun <T> Single<T>.subscribeNormal(
    t: CycleContainer<*>,
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    t.cycle.addObserver(observer)
    this.subscribe(observer)
}


fun <T : Any> Single<T>.subscribeNormal(
    t: ViewModel<*, *>,
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    t.t.cycle.addObserver(observer)
    if (t.disposable == null || t.disposable?.isDisposed!!) {
        this.subscribe(observer)
        t.disposable = observer.disposable.get()
    }
}


fun toast(e: Throwable) {
    val message = e.message
    if (!TextUtils.isEmpty(message))
        Toast.makeText(App.activity(), e.message, Toast.LENGTH_SHORT).show()
}

fun installApkFile(
    context: Context,
    file: File,
    fileProvider: String = "com.customers.zktc.fileProvider"
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


fun htmlText(vararg texts: Text): CharSequence {
    val builder = StringBuilder()
    for (text in texts) builder.append(text)
    return Html.fromHtml(builder.toString())
}

fun h(text: String, color: Int, big: Int = 0, line: Boolean = false): Text {
    return Text(text, color, big, line)
}

fun h(text: String, color: String = "", big: Int = 0, line: Boolean = false): Text {
    return Text(text, color, big, line)
}



