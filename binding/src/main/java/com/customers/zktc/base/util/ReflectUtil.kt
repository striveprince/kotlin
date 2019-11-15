package com.customers.zktc.base.util

import android.text.TextUtils
import timber.log.Timber
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList

/**
 * Company: 中科同创
 * Description:
 * Author: created by WangKeZe on 2019/9/30 12:33
 * Email: wkz0917@163.com
 * Phone: 15390395799
 */
object ReflectUtil {

    inline fun <reified T> toArray(list: List<T>): Array<T> {
        return ArrayList<T>(list).toArray(arrayOf())
    }

    fun invoke(methodName: String, o: Any, vararg args: Any) {
        val cl = ArrayList<Class<*>>()
        for(arg in args)cl.add(arg.javaClass)
        val cs  = toArray(cl)
        val method = getAllClassMethod(o.javaClass, methodName, cs)
        if (method != null) {
            method.isAccessible = true
            invoke(method, o, *args)
        }
    }

    private fun getAllClassMethod(c: Class<*>?, methodName: String, cs: Array<Class<*>>): Method? {
        if (TextUtils.isEmpty(methodName)) return null
        var method: Method? = null
        try {
            method = c?.getDeclaredMethod(methodName, *cs)
        } catch (e: Exception) {
            Timber.v("no such method method:%1s", methodName)
        }
        if (method == null) {
            for (declareMethod in c!!.declaredMethods) {
                if (isValid(methodName, declareMethod, cs)) {
                    method = declareMethod
                    break
                }
            }
        }
        return if (method == null && c != Any::class.java) getAllClassMethod(c!!.superclass, methodName, cs) else method
    }


    operator fun invoke(method: Method, t: Any, vararg args: Any) {
        try {
            method.invoke(t, *args)
        } catch (e: Exception) {

        }

    }


    private fun isValid(methodName: String, declareMethod: Method, cs: Array<Class<*>>): Boolean {
        if (declareMethod.name != methodName) return false
        val params = declareMethod.parameterTypes
        if (cs.size != params.size) return false
        var index = -1
        for (param in params) {
            if (!param.isAssignableFrom(cs[++index])) return false
        }
        return true
    }

    fun isFieldNull(o: Any?): Boolean {
        val value = o.toString()
        return when {
            o == null -> true
            o is Int -> Integer.valueOf(value) == 0
            o is Double -> java.lang.Double.valueOf(value) == 0.0
            o is Long -> java.lang.Long.valueOf(value) == 0L
            o is Char -> o == '\u0000'
            o is String -> TextUtils.isEmpty(o.toString())
            o is Collection<*> -> o.isEmpty()
            o.javaClass.isArray -> (o as Array<*>).size == 0
            else -> false
        }
    }

    fun beanGetValue(f: Field, bean: Any): Any? {
        try {
            val method = beanGetMethod(f, bean.javaClass)
            return method?.invoke(bean)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }


    private fun beanGetMethod(f: Field, c: Class<*>): Method? {
//        return beanMethod(f, c, if (f.type == Boolean::class.javaPrimitiveType) "is" else "get", arrayOf())
        return beanMethod(f, c,
            if (f.type == Boolean::class.javaPrimitiveType&&f.name.toLowerCase(Locale.getDefault()).startsWith("is"))
                "is" else "get", arrayOf())
    }

    private fun beanSetMethod(f: Field, c: Class<*>): Method? {
        return beanMethod(f, c, "set", arrayOf(f.type))
    }

    private fun beanMethod(f: Field, c: Class<*>, prefix: String, params: Array<Class<*>>): Method? {
        f.isAccessible = true
        var fieldName = f.name
        if (fieldName.toLowerCase(Locale.getDefault()).startsWith("is"))
            fieldName = f.name.substring(2,f.name.length)
        val cs = fieldName.toCharArray()
        if (cs[0].toInt() in 97..122){
            cs[0] = (cs[0].toInt()-32).toChar()
        }
        return getAllClassMethod(c, prefix + String(cs), params)
    }

    fun beanSetValue(f: Field, bean: Any, value: Any) {
        try {
            val method = beanSetMethod(f, bean.javaClass)
            method?.invoke(bean, value)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

}