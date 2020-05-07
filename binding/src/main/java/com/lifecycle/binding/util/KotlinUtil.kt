@file:Suppress("UNCHECKED_CAST", "unused")

package com.lifecycle.binding.util

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.ActionMenuView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lifecycle.binding.adapter.AdapterEvent
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.rotate.TimeUtil
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.bind.data.DataBindInflate
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File
import java.lang.RuntimeException

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/14 18:04
 * Email: 1033144294@qq.com
 */


/**
 * FF FF: error
 *      0x0001 error
 *      0x0000 success
 * FF   : run
 *      0x00 end
 *      0x01 start
 *      0x02 running
 * FF   : state
 *      @see AdapterType
 */

//end
fun stateEnd(@AdapterEvent state :Int)= state and 0x100FF
fun isStateEnd(@AdapterEvent state: Int)=state shr 8 and 1 == 0 &&state shr 9 and 1 == 0


//start
fun stateStart(@AdapterEvent state :Int)=state or 0x00100
fun isStateStart(@AdapterEvent state: Int)= state shr 8 and 1 == 1 && !isStateRunning(state)


//running
fun stateRunning(state: Int)=state or 0x0200
fun isStateRunning(@AdapterEvent state :Int)=state shr(9) and 1 == 1


//error
fun stateError(state :Int)= state or 0x010000

//success
fun stateSuccess(state :Int)=state and 0xff

fun isStateSuccess(state :Int)=state shr 16 == 0

fun stateOriginal(state: Int)= state and 0xff

fun Int.stateEqual(@AdapterEvent state: Int)=(this and 0xff) == (state and 0xff)



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


inline fun <reified T : ViewModel> LifecycleOwner.viewModel(): T {
    return if (this is Fragment) ViewModelProvider(this)[T::class.java]
    else ViewModelProvider(this as FragmentActivity)[T::class.java]
}


inline fun <reified E : DataBindInflate<*, out ViewDataBinding>> Any.toEntity(vararg arrayOfAny: Any?): E {
    val clazz = E::class
    val list: ArrayList<Any?> = arrayListOf(this)
    list.addAll(arrayOfAny)
    for (it in clazz.constructors) {
        if (it.parameters.size == list.size) {
            val parameters = list.toArray()
            return it.call(*parameters)
        }
    }
    throw RuntimeException( "check ${E::class.simpleName} class's constructor")
}

inline fun <reified E : DataBindInflate<*, out ViewDataBinding>> List<Any>.toEntities(vararg arrayOfAny: Any?): List<E> {
    val list = ArrayList<E>()
    for (any in this) {
        list.add(any.toEntity(*arrayOfAny))
    }
    return list
}

private fun<T> observableCallback(block: (T) -> Unit) = object : Observable.OnPropertyChangedCallback(){
    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
        val t = when(sender){
            is ObservableInt -> sender.get()
            is ObservableBoolean -> sender.get()
            is ObservableFloat -> sender.get()
            is ObservableByte -> sender.get()
            is ObservableChar -> sender.get()
            is ObservableDouble -> sender.get()
            is ObservableLong -> sender.get()
            is ObservableShort -> sender.get()
            is ObservableField<*> -> sender.get()
            else -> throw RuntimeException("cannot get the value type:${sender.javaClass.simpleName}")
        }
        block(t as T)
    }
}

fun ObservableInt.observe(block: (Int) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableBoolean.observe(block: (Boolean) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableChar.observe(block: (Char) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableShort.observe(block: (Short) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableLong.observe(block: (Long) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableByte.observe(block: (Byte) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableDouble.observe(block: (Double) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun ObservableFloat.observe(block: (Float) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun<T> ObservableField<T>.observe(block: (T) -> Unit): Observable.OnPropertyChangedCallback {
    return observableCallback(block).also { addOnPropertyChangedCallback(it) }
}

fun Context.string(@StringRes id: Int, vararg any: Any) =
    getString(id, *any)

fun Context.drawable(@DrawableRes id:Int) = ContextCompat.getDrawable(this,id)

fun Context.color(@ColorRes id:Int) = ContextCompat.getColor(this,id)

val displayMetrics by lazy { AppLifecycle.application.resources.displayMetrics }
val density by lazy { displayMetrics.density }
val screenWidth by lazy { displayMetrics.widthPixels }
val screenHeight by lazy { displayMetrics.heightPixels }

fun dip(int: Int) = (density*int).toInt()
fun pxToDip(int: Int) = (int/density+0.5).toInt()

fun<T> LiveData<T>.observer(owner: LifecycleOwner,block:(T)->Unit){
    observe(owner, Observer { block(it) })
}

fun findLayoutView(thisCls: Class<*>): LayoutView {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return thisCls.getAnnotation(LayoutView::class.java) ?: findLayoutView(thisCls = thisCls.superclass!!)
}

fun <T,R> List<T>.converter(block: T.() -> R):List<R>{
    val list = ArrayList<R>()
    for (t in this) list.add( t.block())
    return list
}

inline fun <reified T : Activity> Activity.startActivity(vararg pairs: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundleOf(*pairs))
    startActivity(intent)
}


inline fun <reified T> toArray(list: List<T>): Array<T> {
    return ArrayList<T>(list).toArray(arrayOf())
}

@UnstableDefault
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
        val darkModeFlag = field.getInt(layoutParams)
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

fun ViewGroup.layoutParam(width:Int = ViewGroup.LayoutParams.MATCH_PARENT,height:Int = ViewGroup.LayoutParams.WRAP_CONTENT):ViewGroup.LayoutParams{
    return when(this){
        is FrameLayout->FrameLayout.LayoutParams(width, height)
        is LinearLayout->LinearLayout.LayoutParams(width, height)
        is RadioGroup->RadioGroup.LayoutParams(width, height)
        is RelativeLayout->RelativeLayout.LayoutParams(width, height)
        is DrawerLayout-> DrawerLayout.LayoutParams(width, height)
        is androidx.appcompat.widget.Toolbar->androidx.appcompat.widget.Toolbar.LayoutParams(width, height)
        is RecyclerView-> RecyclerView.LayoutParams(width, height)
        is AbsListView->AbsListView.LayoutParams(width, height)
        is TableRow->TableRow.LayoutParams(width, height)
        is ActionBar->ActionBar.LayoutParams(width, height)
        is ActionMenuView->ActionMenuView.LayoutParams(width, height)
        is CoordinatorLayout->CoordinatorLayout.LayoutParams(width, height)
        is CollapsingToolbarLayout->CollapsingToolbarLayout.LayoutParams(width, height)
        else->ViewGroup.LayoutParams(width, height)
    }
}


inline fun <reified T : ViewModel> LifecycleOwner.viewModel(factory: ViewModelProvider.Factory? = null): T {
    return lifeModel(T::class.java,factory)
}

fun <T : ViewModel> LifecycleOwner.lifeModel(clazz: Class<T>, factory: ViewModelProvider.Factory? = null): T {
    return if (factory == null)
        if (this is Fragment) ViewModelProvider(this)[clazz]
        else ViewModelProvider(this as FragmentActivity)[clazz]
    else
        if (this is Fragment) ViewModelProvider(this, factory)[clazz]
        else ViewModelProvider(this as FragmentActivity, factory)[clazz]
}

@Suppress("UNREACHABLE_CODE")
fun <T : ViewModel> LifecycleOwner.lifeViewModel(clazz: Class<T>, vararg argument:Any): T {
    return if(argument.isEmpty())return lifeModel(clazz,null)
    else {
        val factory = object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val a = Array<Class<*>>(argument.size) { argument[it].javaClass }
                return modelClass.getConstructor(*a).newInstance()
            }
        }
        return lifeModel(clazz,factory)
    }
}

fun Activity.softKeyBoardListener(block:(Boolean,Int)->Unit){
    val rootView = window.decorView
    var height = 0
    val listener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val visibleHeight = r.height()
        if (height == 0) height = visibleHeight
        val h =  height - visibleHeight
        when{
            h>200->block(true,h)
            h<-200->block(false,h)
        }
        height = visibleHeight
    }
    rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    rootView.viewTreeObserver.addOnGlobalLayoutListener (listener)
}