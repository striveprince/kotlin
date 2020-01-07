package com.lifecycle.binding.util

import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.edit


fun SharedPreferences.putBundle(bundle: Bundle, commit: Boolean = false) {
    edit(commit) { for (key in bundle.keySet()) putValue(key, bundle[key]) }
}

fun SharedPreferences.putAll(map: Map<String, Any>, commit: Boolean = false) {
    edit(commit) { for (key in map.keys) putValue(key, map[key]) }
}

fun <T> SharedPreferences.Editor.putValue(key: String, it: T) {
    when (it) {
        is Int -> putInt(key, it)
        is String -> putString(key, it)
        is Boolean -> putBoolean(key, it)
        is Float -> putFloat(key, it)
        is Long -> putLong(key, it)
        is Set<*> -> putStringSet(key, it.converter { it.toJson() })
        else -> putString(key, it.toJson())
    }
}

fun SharedPreferences.put(any: Any, commit: Boolean = false) {
    edit(commit) {
        for (field in any.javaClass.getAllFields()) {
            field.isAccessible = true
            runCatching { putValue(field.name, field[any]) }
        }
    }
}

inline fun <reified T> SharedPreferences.get(t:T = T::class.java.newInstance()):T{
    for (field in T::class.java.getAllFields()) {
        beanFieldSet(field, t as Any, all[field.name])
    }
    return t
}