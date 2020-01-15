package com.lifecycle.binding.util

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.lifecycle.binding.util.toArray
import timber.log.Timber
import java.lang.StringBuilder
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        Timber.v("no such method method: $methodName(${cs.params()})")
        for (declareMethod in declaredMethods) {
            if (isValid(methodName, declareMethod, cs)) return declareMethod
        }
        if (this == Any::class.java) null else superclass!!.getAllMethod(methodName, cs)
    }
}

fun Array<Class<*>>.params(): String {
    val b = StringBuilder()
    forEachIndexed { index, clazz -> b.append(clazz.simpleName).append(":").append("arg").append(index) }
    return b.toString()
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
    params.forEachIndexed { index, param -> if (cs[index].let { !param.isAssignableFrom(it)&&!it.baseType() }) return false  }
    return true
}
private fun Class<*>.baseType():Boolean{
    return when(kotlin){
//        int.class
        Int::class,Double::class,Long::class,Char::class,Byte::class,Float::class,Boolean::class,Short::class-> true
        else -> false
    }

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
    return beanMethod(f.name, c, prefix, params)
//    var fieldName = f.noDelegateName()
//    if (fieldName.toLowerCase(Locale.getDefault()).startsWith("is"))
//        fieldName = f.noDelegateName().substring(2, f.name.length)
//    val cs = fieldName.toCharArray()
//    if (cs[0].toInt() in 97..122)
//        cs[0] = (cs[0].toInt() - 32).toChar()
//    return c.getAllMethod(prefix + String(cs), params)
}

fun beanSetValue(f: Field, bean: Any, value: Any) {
    runCatching { beanSetMethod(f, bean.javaClass)?.invoke(bean, value) }
}

fun beanSetValue(f: String, bean: Any, value: Any) {
    runCatching { beanSetMethod(f, bean.javaClass, value.javaClass)?.invoke(bean, value) }
}

private fun beanSetMethod(f: String, c: Class<*>, type: Class<*>): Method? {
    return beanMethod(f, c, "set", arrayOf(type))
}


private fun beanMethod(fieldName: String, c: Class<*>, prefix: String, params: Array<Class<*>>): Method? {
    return with(fieldName.toLowerCase(Locale.getDefault())) {
        substring(if (startsWith("is")) 2 else 0)
            .toCharArray()
            .toUpperChar()
            .let { c.getAllMethod(prefix + String(it), params) }
    }
}

fun CharArray.toUpperChar(): CharArray {
    return let {
        if (it[0].toInt() in 97..122) {
            it[0] = (it[0].toInt() - 32).toChar()
        }
        it
    }
}


fun Field.noDelegateName() = name.replace("\$delegate", "")

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun <reified T, reified R> T.copy(r: R): T {
    val map = HashMap<String, Any>()
    R::class.java.declaredFields.forEach { runCatching { map[it.name] = it.get(r) } }
    T::class.java.declaredFields.forEach { field ->
        field.apply {
            isAccessible = true
            map[name]?.let { set(this@copy, it) }
        }
    }
    return this
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
    beanFieldSet(bean.javaClass.getDeclaredField(fieldName), bean, value)
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