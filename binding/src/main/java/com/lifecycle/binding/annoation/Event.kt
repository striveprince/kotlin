package com.lifecycle.binding.annoation

interface Event {
}

/**
 *
 *
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
val event:Boolean = false,
vararg val layout:Int
)
 * */