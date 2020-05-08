package com.lifecycle.binding.life

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.util.*
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
interface LifecycleInit <Model:ViewModel>:Init,LifecycleOwner{
    fun initModel(clazz: Class<*> = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<*>):Model=lifeModel(clazz as Class<Model>)
    fun owner():LifecycleOwner = this
    fun requireActivity(): FragmentActivity
    fun inject(savedInstanceState: Bundle?):View

    fun Int.stringRes(vararg any: Any)= requireActivity().string(this,*any)
    fun Int.drawable() = requireActivity().drawable(this)
    fun Int.color() = requireActivity().color(this)
}