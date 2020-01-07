package com.lifecycle.binding.util

import android.text.TextUtils
import timber.log.Timber
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList

/**
 * Company:
 * Description:
 */

fun invoke(methodName: String, bean: Any, vararg args: Any) {
    val cl = ArrayList<Class<*>>()
    for (arg in args) cl.add(arg.javaClass)
    val cs = toArray(cl)
    val method = bean.javaClass.getAllMethod(methodName, cs)
    if (method != null) {
        method.isAccessible = true
        invoke(method, bean, *args)
    }
}

fun Class<*>.getAllMethod(methodName: String, cs: Array<Class<*>>): Method? {
    return try {
        if (methodName.isEmpty()) null else getDeclaredMethod(methodName, *cs)
    } catch (e: Exception) {
        Timber.v("no such method method:%1s", methodName)
        for (declareMethod in declaredMethods) {
            if (isValid(methodName, declareMethod, cs)) return declareMethod
        }
        if (this == Any::class.java) null else superclass!!.getAllMethod(methodName, cs)
    }
}

fun Class<*>.getAllFields(): List<Field> {
    val list = ArrayList<Field>()
    for (declaredField in declaredFields) {
        list.add(declaredField)
    }
    if (this != Any::class.java) list.addAll(superclass!!.getAllFields())
    return list
}

fun invoke(method: Method, t: Any, vararg args: Any) {
    try {
        method.invoke(t, *args)
    } catch (e: Exception) {
        Timber.v("$method function invoke failed")
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
    return when {
        o == null -> true
        o is Int -> Integer.valueOf(o) == 0
        o is Double -> java.lang.Double.valueOf(o) == 0.0
        o is Long -> java.lang.Long.valueOf(o) == 0L
        o is Char -> o == '\u0000'
        o is String -> TextUtils.isEmpty(o.toString())
        o is Collection<*> -> o.isEmpty()
        o.javaClass.isArray -> (o as Array<*>).size == 0
        else -> false
    }
}

fun beanGetValue(f: Field, bean: Any): Any? {
    return runCatching { beanGetMethod(f, bean.javaClass)?.invoke(bean) }.getOrNull()
}


fun beanGetMethod(f: Field, c: Class<*>): Method? {
    val prefix = if (f.type == Boolean::class.javaPrimitiveType && f.name.toLowerCase(Locale.getDefault()).startsWith("is")) "is" else "get"
    return beanMethod(f, c, prefix, arrayOf())
}

private fun beanSetMethod(f: Field, c: Class<*>): Method? {
    return beanMethod(f, c, "set", arrayOf(f.type))
}

private fun beanMethod(f: Field, c: Class<*>, prefix: String, params: Array<Class<*>>): Method? {
    f.isAccessible = true
    var fieldName = f.name
    if (fieldName.toLowerCase(Locale.getDefault()).startsWith("is"))
        fieldName = f.name.substring(2, f.name.length)
    val cs = fieldName.toCharArray()
    if (cs[0].toInt() in 97..122)
        cs[0] = (cs[0].toInt() - 32).toChar()
    return c.getAllMethod(prefix + String(cs), params)
}

fun beanSetValue(f: Field, bean: Any, value: Any) {
    runCatching { beanSetMethod(f, bean.javaClass)?.invoke(bean, value) }
}

fun beanFieldGet(fieldName: String, bean: Any): Any? {
    return runCatching {
        bean.javaClass.getDeclaredField(fieldName).let {
            it.isAccessible = true
            it.get(bean)
        }
    }.getOrNull()
}

fun beanFieldSet(fieldName: String, bean: Any, value: Any?) {
    beanFieldSet(bean.javaClass.getDeclaredField(fieldName),bean,value)
}

fun beanFieldSet(field: Field, bean: Any, value: Any?) {
    value?.apply {
        runCatching {
            field.let {
                it.isAccessible = true
                it.set(bean, it)
            }
        }
    }
}