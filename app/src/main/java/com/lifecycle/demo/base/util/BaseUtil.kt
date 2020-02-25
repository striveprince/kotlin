@file:Suppress("UNCHECKED_CAST")

package com.lifecycle.demo.base.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.lifecycle.binding.inter.bind.data.DataBindRecycler
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.PhoneSystemManager.Companion.permission
import com.lifecycle.demo.base.util.PhoneSystemManager.Companion.type
import com.lifecycle.demo.inject.data.net.InfoEntity
import com.lifecycle.demo.inject.data.net.exception.ApiException
import com.lifecycle.demo.inject.data.net.exception.NoPermissionException
import com.lifecycle.demo.inject.data.net.transform.ErrorSingleTransformer
import com.lifecycle.demo.inject.data.net.transform.NoErrorObservableTransformer
import com.lifecycle.demo.inject.data.net.transform.RestfulSingleTransformer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.security.MessageDigest
import java.util.regex.Pattern


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 11:09
 * Email: 1033144294@qq.com
 */

fun Context.string(@StringRes id: Int, vararg any: Any) =
    getString(id, *any)


inline fun <reified T : ViewModel> LifecycleOwner.viewModel(): T {
    return if (this is Fragment) ViewModelProvider(this)[T::class.java]
    else ViewModelProvider(this as FragmentActivity)[T::class.java]
}

inline fun <reified E : DataBindRecycler<*, out ViewDataBinding>> Any.toEntity(vararg arrayOfAny: Any?): E {
    val clazz = E::class
    val list: ArrayList<Any?> = arrayListOf(this)
    list.addAll(arrayOfAny)
    for (it in clazz.constructors) {
        if (it.parameters.size == list.size) {
            val parameters = list.toArray()
            return it.call(*parameters)
        }
    }
    throw ApiException(0, "check ${E::class.simpleName} class's constructor")
}

inline fun <reified E : DataBindRecycler<*, out ViewDataBinding>> List<Any>.toEntities(vararg arrayOfAny: Any?): List<E> {
    val list = ArrayList<E>()
    for (any in this) {
        list.add(any.toEntity(*arrayOfAny))
    }
    return list
}


@Suppress("DEPRECATED_IDENTITY_EQUALS")
fun Context.checkPermissions(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(this, permission) !== PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}


fun headUrl(urlString: String): GlideUrl {
    var url = urlString
    if (TextUtils.isEmpty(url)) url = "1"
    return GlideUrl(
        url, LazyHeaders.Builder()
            //.addHeader("referer", "")
            .build()
    )
}

fun md5(paramString: String): String {
    return try {
        val arrayOfByte = MessageDigest.getInstance("MD5")
            .digest(paramString.toByteArray(charset("UTF-8")))
        val stringBuilder = StringBuilder()
        for (b in arrayOfByte.indices) {
            val str = Integer.toHexString(arrayOfByte[b].toInt() and 0xFF)
            if (str.length == 1)
                stringBuilder.append("0")
            stringBuilder.append(str)
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        ""
    }

}

fun <T> Single<T>.noError(): Observable<T> {
    return this.toObservable().compose(NoErrorObservableTransformer())
}

fun <T> Observable<T>.noError(): Observable<T> {
    return this.compose(NoErrorObservableTransformer())
}

//fun <T> Any?.beanFieldGet(fieldName: String, default: T): T {
//    return this?.let {
//        beanFieldGet(fieldName, it) as T
//    } ?: default
//}


fun <T> Single<InfoEntity<T>>.restful(): Single<T> {
    return this.compose(ErrorSingleTransformer())
        .compose(RestfulSingleTransformer())
}


fun <T> Single<InfoEntity<T>>.restfulUI(): Single<T> {
    return this.restful()
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.transformerUI(v: View? = null): Single<T> {
    v?.isEnabled = false
    return this.compose(ErrorSingleTransformer())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { v?.isEnabled = true }
}

fun <T> Observable<T>.checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<T> {
    return if (activity.checkPermissions(*permissions)) this
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) this
            else Observable.error(
                ApiException(1, activity.getString(R.string.no_permission))
            )
        }
}

fun checkPermission(
    activity: Activity,
    vararg permissions: String
): Observable<Boolean> {
    return if (activity.checkPermissions(*permissions)) Observable.just(true)
    else RxPermissions(activity)
        .request(*permissions)
        .flatMap {
            if (it) Observable.just(true)
            else Observable.error(
                ApiException(1, activity.getString(R.string.no_permission))
            )
        }
}

fun Activity.rxPermissions(vararg permissions: String): Observable<Boolean> {
    return RxPermissions(this)
        .request(*permissions)
        .doOnNext { if (!it) throw NoPermissionException() }
}

fun Activity.rxPermissionsDialog(vararg permissions: String): Observable<Boolean> {
    return RxPermissions(this)
        .request(*permissions)
        .flatMap {
            if (it) Observable.just(it)
            else showPermissionDialog(*permissions)
        }
}

private fun Activity.showPermissionDialog(vararg permissions: String): Observable<Boolean> {
    startActivity<PhoneSystemManager>(type to permission)
    return Observable.create<Any> { emitter ->
        MaterialDialog(this)
            .title(R.string.request_permission)
            .positiveButton { PhoneSystemManager.emitter = emitter }
            .show {}
    }.map { checkPermissions(*permissions) }
}

inline fun <reified T : Activity> Activity.startActivity(vararg pairs: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundleOf(*pairs))
    startActivity(intent)
}


const val phone = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$"
fun getPhoneError(mobiles: String): String? {
    if (TextUtils.isEmpty(mobiles)) return "手机号不能为空"
    val p = Pattern.compile(phone)
    val m = p.matcher(mobiles)
    val valid = m.matches()
    return if (valid) null else "不合法的手机号"
}

fun getAccountError(account: String): String? {
    return if (account.isEmpty()) "请输入账号" else null
}

fun getCodeError(code: String): String? {
    if (TextUtils.isEmpty(code)) return "验证码不能为空"
    return if (code.length in 4..6) null else "验证码长度为4-6位"
}

fun getPasswordError(password: String): String? {
    if (TextUtils.isEmpty(password)) return "密码不能为空"
    return null
//    return if (password.length>5)null else "密码长度不能小于6位"
}

fun showInputMethod(context: Context): Boolean {
    return if (isShowing(context)) false
    else {
        showOrHide(context)
        true
    }
}

fun showOrHide(context: Context) {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}


fun isShowing(context: Context): Boolean {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isActive
}


@SuppressLint("PrivateApi")
fun applyKitKatTranslucency(activity: AppCompatActivity, color: Int) {
    setMeizuStatusBarDarkIcon(activity, true)//确定状态栏的字体演示是否为黑色,false为白色，true为黑色，目前6.0以下仅仅适配了两个机型
    setMiuiStatusBarDarkMode(activity, true)
    setAllUpSixVersion(activity) //6.0以上系统
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        setTranslucentStatus(activity)
    }
    //6.0以上，修改状态栏为透明色
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        runCatching {
            val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
            decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor").apply {
                isAccessible = true
                setInt(activity.window.decorView, Color.TRANSPARENT)  //改为透明
            }
        }
    }
//    val tintManager = SystemBarTintManager(activity)
//    tintManager.isStatusBarTintEnabled = true
//    tintManager.setStatusBarTintResource(color)
}


@TargetApi(19)
private fun setTranslucentStatus(activity: AppCompatActivity) {
    val win = activity.window
    val winParams = win.attributes
    val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    winParams.flags = winParams.flags or bits
//    else winParams.flags and bits.inv()
    win.attributes = winParams
}

fun setAllUpSixVersion(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

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

fun setMeizuStatusBarDarkIcon(activity: Activity?, dark: Boolean): Boolean {
    var result = false
    if (activity != null) {
        try {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = (if (dark) value or bit
            else value and bit.inv())
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        } catch (e: Exception) {
        }
    }
    return result
}

fun <T, R> Observable<List<T>>.concatIterable(block: T.() -> R): Observable<R> =
    this.concatMapIterable {it}
        .map { block(it) }

fun <T, R> Observable<List<T>>.concatList(block: T.() -> R): Observable<List<R>> =
    this.concatIterable(block)
        .toList()
        .toObservable()

fun <T> Single<T>.ioToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.newToMainThread(): Single<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}
