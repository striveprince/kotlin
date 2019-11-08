//package com.lifecycle.binding.inflate.inter
//
//import android.content.Context
//import android.view.ViewGroup
//import org.jetbrains.anko.AnkoComponent
//
//interface Anko<Component : AnkoComponent<*>> : Parse<Component> {
//
//    override fun attachView(context: Context, viewGroup: ViewGroup?, attachToParent: Boolean, t: Any?): Component {
//        return binding
//    }
//
//    fun bind(javaClass: Class<Component>, context: Context, co: ViewGroup, attachToParent: Boolean, component: Component?): Component? {
//        val ankoComponent = javaClass.newInstance()
//        return ankoComponent
//    }
//
//    fun component(context: Context) {
//
//    }
//}