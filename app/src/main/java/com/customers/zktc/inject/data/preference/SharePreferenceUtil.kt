package com.customers.zktc.inject.data.preference

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.binding.model.Config
import com.binding.model.ReflectUtil

import java.lang.reflect.Field

import timber.log.Timber

/**
 * project：cutv_ningbo
 * description：
 * create developer： Arvin
 * create time：2015/12/15 10:44.
 * modify developer：  Arvin
 * modify time：2015/12/15 10:44.
 * modify remark：
 *
 * @version 2.0
 */
class SharePreferenceUtil private constructor(context: Context, name: String) {
    private val share: SharedPreferences
    private val editor: SharedPreferences.Editor


    val all: Map<String, *>
        get() = share.all

    init {
        val application = if (context is Application) context else context.applicationContext
        share = application.getSharedPreferences(name, Activity.MODE_PRIVATE)
        editor = share.edit()
        editor.apply()
    }

    fun clear() {
        editor.clear()
        editor.commit()
    }

    fun remove(key: String) {
        editor.remove(key)
        editor.commit()
    }

    private fun putValue(key: String, value: Any?, remove: Boolean) {
        if (value == null) return
        when (value) {
            is String -> editor.putString(key, value.toString())
            is Boolean -> editor.putBoolean(key, (value as Boolean?)!!)
            is Float -> editor.putFloat(key, (value as Float?)!!)
            is Int -> editor.putInt(key, (value as Int?)!!)
            is Long -> editor.putLong(key, (value as Long?)!!)
            is Set<*> -> editor.putStringSet(key, value as Set<String>?)
//            else -> {
//                val clazz = value.javaClass
//                val fields = clazz.declaredFields
//                for (f in fields) {
//                    val obj = ReflectUtil.beanGetValue(f, value)
//                    if (!ReflectUtil.isFieldNull(obj))
//                        putValue(f.name, obj, remove)
//                    else if (remove) editor.remove(f.name)
//                }
//            }
        }
        Timber.w("key:%1s,value:%2s", key, value)
    }

    /**
     * put the information to the memory,please put the value class is instanceof String,boolean,float,long,int and Set<String>
     * can'model put the other type to the memory,otherwise,the method will not set the value to the memory,and if the value is
     * instanceof Set ,but It isn'model instanceof Set<String>,this will throw activity ClassCastException;
     *
     * @param key   key
     * @param value value
    </String></String> */
    fun setValue(key: String,  value: Any) {
        putValue(key, value, false)
        editor.commit()
    }

    fun <T : Any> setAllDto(t: T) {
        val clazz = t.javaClass
        val fields = clazz.declaredFields
        for (f in fields) {
            val obj = ReflectUtil.beanGetValue(f, t)
            if (!ReflectUtil.isFieldNull(obj))
                putValue(f.name, obj, true)
            else
                editor.remove(f.name)
        }
        editor.commit()
    }

    fun <T : Any> setNotNullDto(t: T) {
        val clazz = t.javaClass
        val fields = clazz.declaredFields
        for (f in fields) {
            val `object` = ReflectUtil.beanGetValue(f, t)
            if (!ReflectUtil.isFieldNull(`object`))
                putValue(f.name, `object`, false)
        }
        editor.commit()
    }


    fun <T> newInstance(clazz: Class<T>): T {
        try {
            return clazz.newInstance()
        } catch (e: Exception) {
            throw RuntimeException(
                "please check the construct of the " + clazz.name +
                        ", \tthe class of construct must be have activity no parameter ", e
            )
        }

    }

    fun <T: Any> getAllDto(c: Class<T>, share: SharedPreferences): T {
        val t = newInstance(c)
        return getAllDto(t, share)
    }

    fun <T : Any> getAllDto(t: T, share: SharedPreferences): T {
        val fields = t.javaClass.declaredFields
        for (f in fields) {
            val value = getValue(f.name, f.type, share)
            if (!ReflectUtil.isFieldNull(value))
                ReflectUtil.beanSetValue(f, t, value!!)
        }
        return t

    }

    fun<T:Any> getAllDto(t:T):T{
        return getAllDto(t, share)
    }

    fun <T: Any> getAllDto(clazz: Class<T>): T {
        return getAllDto(clazz, share)
    }

    fun <E> getValue(key: String, clazz: Class<E>): E? {
        return getValue(key, clazz, share)
    }

    /**
     * 遍历Share中的key值，让其与实体类属性名匹配,匹配成功则取出值
     */
    @Suppress("UNCHECKED_CAST")
    private fun <E> getValue(key: String, clazz: Class<E>, share: SharedPreferences): E? {
        if (clazz == String::class.java) {
            return share.getString(key, "") as E
        } else if (clazz == Int::class.javaPrimitiveType || clazz == Int::class.java) {
            val result = share.getInt(key, 0)
            return result as E
        } else if (clazz == Long::class.javaPrimitiveType || clazz == Long::class.java) {
            val result = share.getLong(key, 0)
            return result as E
        } else if (clazz == Boolean::class.javaPrimitiveType || clazz == Boolean::class.java) {
            val result = share.getBoolean(key, false)
            return result as E
        } else if (clazz == Float::class.javaPrimitiveType || clazz == Float::class.java) {
            val result = share.getFloat(key, 0f)
            return result as E
        } else if (clazz == Set::class.java) {
            val result = share.getStringSet(key, null)
            return result as E
        }
        return null
    }

    fun getString(key: String): String? {
        val value = share.getString(key, "")
        Timber.i("key:%1s,value:%2s", key, value)
        return value

    }

    fun getInt(key: String): Int {
        val value = share.getInt(key, 0)
        Timber.i("key:%1s,value:%2d", key, value)
        return value
    }

    fun getInt(key: String, i: Int): Int {
        return share.getInt(key, i)
    }

    fun getBoolean(first_open: String, b: Boolean): Boolean {
        return share.getBoolean(first_open, b)
    }

    companion object {
        private var userShare: SharePreferenceUtil? = null
        private var settingShare: SharePreferenceUtil? = null
        fun getUserInstance(context: Context): SharePreferenceUtil {
            var util = userShare
            if (util == null) {
                synchronized(SharePreferenceUtil::class.java) {
                    util = userShare
                    if (util == null) {
                        util = SharePreferenceUtil(context, "user")
                        userShare = util
                    }
                }
            }
            return util!!
        }

        fun getSettingInstance(context: Context): SharePreferenceUtil {
            var util = settingShare
            if (util == null) {
                synchronized(SharePreferenceUtil::class.java) {
                    util = settingShare
                    if (util == null) {
                        util = SharePreferenceUtil(context, "setting")
                        settingShare = util
                    }
                }
            }
            return util!!
        }
    }
}
