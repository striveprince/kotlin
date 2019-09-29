package com.binding.model.annoation


@Target(AnnotationTarget.TYPE,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
    val event: String = "",
    vararg val layout: Int
)