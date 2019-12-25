package com.lifecycle.binding.inter.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.util.findLayoutView
import kotlin.reflect.KClass
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.jvm.javaType

interface Binding<T,B:ViewBinding>:Parse<T, B> {
//    @Suppress("UNCHECKED_CAST")
//    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
//        val clazz = javaClass.kotlin.supertypes[0].arguments[1].type!!.classifier as KClass<B>
//        val inflate = clazz.staticFunctions.find { it.name == "inflate"&&it.parameters.size == 3 }!!
//        return inflate.call(LayoutInflater.from(context),parent,attachToParent) as B
//    }


    @Suppress("UNCHECKED_CAST")
    override fun parse(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): B {
        val clazz = javaClass.kotlin.supertypes[0].arguments[1].type!!.javaType as Class<B>
        val method = clazz.getMethod("inflate",LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
        return method.invoke(null,LayoutInflater.from(context),parent,attachToParent) as B
    }

    override fun createView(t: T, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        return parse(t,context,parent, attachToParent).root
    }
}