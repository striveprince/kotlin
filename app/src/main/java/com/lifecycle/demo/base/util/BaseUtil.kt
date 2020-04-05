@file:Suppress("UNCHECKED_CAST")

package com.lifecycle.demo.base.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.demo.ui.DemoApplication
import java.security.MessageDigest
import java.util.regex.Pattern


/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/10/11 11:09
 * Email: 1033144294@qq.com
 */


val api = (AppLifecycle.application as DemoApplication).api





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
