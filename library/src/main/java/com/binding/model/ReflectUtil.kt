package com.binding.model

import android.text.TextUtils
import timber.log.Timber
import java.lang.reflect.Method

/**
 * Company: 中科同创
 * Description:
 * Author: created by WangKeZe on 2019/9/30 12:33
 * Email: wkz0917@163.com
 * Phone: 15390395799
 */
object ReflectUtil {

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
            method = c!!.getDeclaredMethod(methodName, *cs)
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
        return if (method == null && c != Any::class.java) getAllClassMethod(
            c!!.superclass,
            methodName,
            cs
        ) else method
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

}