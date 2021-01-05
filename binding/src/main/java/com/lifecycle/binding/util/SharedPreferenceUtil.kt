@file:Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")

package  com.lifecycle.binding.util

import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONArray
import timber.log.Timber
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.full.superclasses


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
        is Collection<*> -> putStringSet(key, it.converter { toJsonWithoutBaseType(it) })
        is ObservableProperty<*> -> it.value()?.let { putValue(key, it) }
        else -> putString(key, it.toJson())
    }
}

fun ObservableProperty<*>.value(): Any? = runCatching { javaClass.kotlin.superclasses[0].java.declaredFields[0].apply { isAccessible = true }.get(this@value) }.getOrNull()

private fun <T> toJsonWithoutBaseType(it: T): String {
    return when (it) {
        null -> ""
        is Int, Boolean, Double, Float, Byte, Long, String, Char -> it.toString()
        else -> it.toJson()
    }
}


fun <T> SharedPreferences.vetoable(t: T, commit: Boolean = false, block: (T, T) -> Boolean = { o, n -> o != n }): ReadWriteProperty<Any?, T> {
    return Delegates.vetoable(t) { k, o, n ->
        block(o, n).apply { if (this) putPair(k.name to n as Any, commit = commit) }
    }
}

fun SharedPreferences.putPair(vararg pairs: Pair<String, Any>, commit: Boolean = false) {
    putBundle(bundleOf(*pairs), commit)
}

fun SharedPreferences.putValue(key: String, value: Any, commit: Boolean = false) {
    edit(commit) { putValue(key, value) }
}

fun SharedPreferences.put(any: Any, commit: Boolean = false) {
    edit(commit) {
        for (field in any.javaClass.getAllFields()) {
            field.isAccessible = true
            runCatching { putValue(field.noDelegateName(), field[any]) }
        }
        Timber.i("$all")
    }
}

fun SharedPreferences.clear(commit: Boolean = false) {
    edit(commit) { clear() }
}

inline fun <reified T> SharedPreferences.get(t: T = T::class.java.newInstance()): T {
    for (field in T::class.java.getAllFields()) {
        all[field.noDelegateName()]?.let {
            val value = if (isJson(it)) gson.fromJson(it as String, field.genericType) else it
            beanSetValue(field.noDelegateName(), value, it)
        }
    }
    return t
}

inline fun <reified T> SharedPreferences.get(key: String, t: T? = null): T {
    return when (T::class) {
        Boolean::class -> getBoolean(key, t as Boolean) as T
        Int::class -> getInt(key, t as Int) as T
        Float::class -> getFloat(key, t as Float) as T
        String::class -> getString(key, t as String) as T
        Long::class -> getLong(key, t as Long) as T
        Set::class -> getStringSet(key, defaultSet(t)) as T
        else -> getString(key, t?.toJson())?.fromJson<T>() ?: get()
    }
}

fun <T> defaultSet(t: T?): Set<String>? {
    return if (t !is Collection<*>) setOf(toJsonWithoutBaseType(t))
    else return HashSet<String>().apply { forEach { add(toJsonWithoutBaseType(it)) } }
}


inline fun <reified T> SharedPreferences.liveData(key: String, t: T): MutableLiveData<T> {
    return object : MutableLiveData<T>() {
        override fun getValue() = super.getValue() ?: get(key, t).apply { setValue(t) }
        override fun setValue(value: T) {
            super.setValue(value)
            putValue(key, value as Any)
        }
    }
}



fun isJson(json: Any): Boolean {
    if (json is String) {
        val jsonElement: JsonElement = try {
            JsonParser().parse(json)
        } catch (e: Exception) {
            return false
        }
        return jsonElement.isJsonObject
    }
    return false
}
//
//open class SharedPreferenceLiveData<T>:LiveData<T>(){
//    override fun getValue()=super.getValue()
//
//    public override fun setValue(value: T) {
//        super.setValue(value)
//    }
//}


