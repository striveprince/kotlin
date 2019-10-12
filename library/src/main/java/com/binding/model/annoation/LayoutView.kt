package com.binding.model.annoation


@Target(AnnotationTarget.TYPE,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
    val event: Boolean = false,
    vararg val layout: Int
)