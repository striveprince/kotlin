package com.lifecycle.binding

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.annoation.LayoutView
import com.lifecycle.binding.base.RxBus
import com.lifecycle.binding.base.Text
import com.lifecycle.binding.inflate.inter.Entity
import com.lifecycle.binding.inflate.observer.NormalObserver
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
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File
import java.lang.RuntimeException


const val pageWay = true
val gson = Gson()

fun findModelView(thisCls: Class<*>): LayoutView {
    return thisCls.getAnnotation(LayoutView::class.java)
        ?: return findModelView(thisCls.superclass as Class<*>)
}




inline fun <reified T> toArray(list: List<T>): Array<T> {
    return ArrayList<T>(list).toArray(arrayOf())
}

@ImplicitReflectionSerializer
inline fun <reified T : Any> parse(string: String): T {
    return Json.parse(string)
}

fun <T, R> Observable<List<T>>.concatIterable(block: T.() -> R): Observable<R> =
    this.concatMap { Observable.fromIterable(it) }
        .map { block.invoke(it) }

fun <T, R> Observable<List<T>>.concatList(block: T.() -> R): Observable<List<R>> =
    this.concatMap { Observable.fromIterable(it) }
        .map { block.invoke(it) }
        .toList()
        .toObservable()

inline fun <reified E> rxBus(): Observable<E> {
    return RxBus.getInstance().toObservable(E::class.java)
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

fun <T> Observable<T>.subscribeNormal(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = {},
    onSubscribe: (Disposable) -> Unit = {}
) {
    this.subscribe(NormalObserver(onNext, onError, onComplete, onSubscribe))
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

inline fun<reified Model:ViewModel> provideViewModel(appCompatActivity: AppCompatActivity) =
    ViewModelProviders.of(appCompatActivity).get(Model::class.java)


fun <T> Single<T>.subscribeNormal(
    v: View,
    lifecycle: Lifecycle = viewLifeCycle(v),
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { toast(it) },
    onComplete: () -> Unit = { v.isEnabled = true },
    onSubscribe: (Disposable) -> Unit = { v.isEnabled = false }
) {
    val observer = NormalObserver(onNext, onError, onComplete, onSubscribe)
    lifecycle.addObserver(observer)
    this.subscribe(observer)
}

fun viewLifeCycle(view: View): Lifecycle {
    val context = view.context
    if (context is AppCompatActivity) return context.lifecycle
    else throw RuntimeException("当前的context不属于AppCompatActivity")
}

fun toast(e: Throwable) {
    if(!TextUtils.isEmpty(e.message))
        toast(e.message!!)
}


fun toast(message: String) {
    Toast.makeText(App.activity(), message, Toast.LENGTH_SHORT).show()
}

fun installApkFile(
    context: Context,
    file: File,
    fileProvider: String = "com.lifecycle.binding.fileProvider"
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

inline fun <reified E : Entity<*, out ViewDataBinding>> Any.toEntity(vararg arrayOfAny:Any?): E {
    val list:ArrayList<Any?> = arrayListOf(this)
    list.addAll(arrayOfAny)
    E::class.constructors.forEach {
        if (it.parameters.size == list.size) {
            val parameters = list.toArray()
            return it.call(*parameters!!)
        }
    }
    throw RuntimeException("check ${E::class.simpleName} class's constructor")
}

inline fun <reified E:Entity<*,out ViewDataBinding>> List<Any>.toEntities(vararg arrayOfAny:Any?):List<E>{
    val list = ArrayList<E>()
    for (any in this) {
        list.add(any.toEntity(*arrayOfAny))
    }
    return list
}

//CoroutineScope
//
//@JvmOverloads
//@UseExperimental(ExperimentalTypeInference::class, ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
//fun <E> rxPublisher(
//    context: CoroutineContext = kotlin.coroutines.EmptyCoroutineContext,
//    capacity: Int = Channel.RENDEZVOUS,
//    @BuilderInference block: suspend ProducerScope<E>.() -> Unit
//): Publisher<E> =
//    Publisher { subscriber ->
//        if (subscriber == null) throw NullPointerException("Subscriber cannot be null")
//        val rxPublisherCoroutine = Rx2PublisherCoroutine(subscriber, context, capacity)
//        subscriber.onSubscribe(rxPublisherCoroutine)
//        rxPublisherCoroutine.start(block)
//    }
//
//private class Rx2PublisherCoroutine<E>(val subscriber: Subscriber<E>, context: CoroutineContext, val capacity: Int) : Subscription, CoroutineScope by CoroutineScope(context), ProducerScope<E> {
//
//    private val mChannel = Channel<E>(capacity)
//
//    private val mutex = Mutex(locked = true)
//
//    fun start(block: suspend ProducerScope<E>.() -> Unit) {
//        launch {
//            mChannel.consumeEach { elem ->
//                try {
//                    if (capacity != Channel.RENDEZVOUS) {
//                        mutex.lock()
//                    }
//                    subscriber.onNext(elem)
//                } catch (e: Throwable) {
//                    dispose(e)
//                    throw e //rethrow
//                }
//            }
//        }
//
//        launch {
//            block()
//        }
//    }
//
//    fun dispose(cause: Throwable?) {
//        try {
//            cancel(if (cause is CancellationException) cause else null) // CoroutineScope.cancel
//            close(cause) // this.close
//            if (cause != null && cause !is CancellationException) {
//                subscriber.onError(cause)
//            } else {
//                subscriber.onComplete()
//            }
//        } finally {
//            mutex.unlock()
//        }
//    }
//
//
//    override val channel: SendChannel<E>
//        get() = mChannel
//
//    @ExperimentalCoroutinesApi
//    override val isClosedForSend: Boolean
//        get() = channel.isClosedForSend
//
//    @ExperimentalCoroutinesApi
//    override val isFull: Boolean
//        get() = mutex.isLocked
//
//    override val onSend: SelectClause2<E, SendChannel<E>>
//        get() = channel.onSend
//
//    override fun close(cause: Throwable?): Boolean {
//        return channel.close()
//    }
//
//    @ExperimentalCoroutinesApi
//    override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) {
//        return channel.invokeOnClose(handler)
//    }
//
//    override fun offer(element: E): Boolean {
//        if (capacity == Channel.RENDEZVOUS && !mutex.tryLock()) return false
//        return channel.offer(element)
//    }
//
//    override suspend fun send(element: E) {
//        if (capacity == Channel.RENDEZVOUS) {
//            mutex.lock()
//        }
//        channel.send(element)
//    }
//
//    override fun cancel() {
//        dispose(null)
//    }
//
//    override fun request(n: Long) {
//        mutex.unlock()
//    }
//}