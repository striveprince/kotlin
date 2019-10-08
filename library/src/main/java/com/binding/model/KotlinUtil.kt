package com.binding.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.binding.model.annoation.Event
import com.binding.model.annoation.LayoutView
import java.lang.StringBuilder

val pageWay  = false

fun findModelView(thisCls: Class<*>): LayoutView {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return thisCls.getAnnotation(LayoutView::class.java) ?:return findModelView(thisCls = thisCls.superclass)
}
inline fun <reified T> toArray(list: List<T>):Array<T>{
    return ArrayList<T>(list).toArray(arrayOf())
}

val gson = Gson()

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>(){}.type)
inline fun <reified T> String.fromGson() = gson.fromJson<T>(this)

fun contain(value: Int, min: Int, max: Int): Boolean {
    return value in min until max
}

fun containsList(value: Int, list: List<*>): Boolean {
    return contain(value, 0, list.size)
}

 inline fun <T, R> T.transform(block: T.() -> R): R {
    return block()
}

//enum class E :Event{
//    a,b,c,d
//}
//fun t(){
//    event(E.a, E.b, E.c, E.d)
//}
//
//fun event(vararg event:Event):String{
//    val s =StringBuilder()
//    for (event in event) {
//        s.append(event.toString())
//        s.append("|")
//    }
//    s.deleteCharAt(s.length-1)
//    return s.toString()
//}
