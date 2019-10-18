package com.binding.model.annoation


@Target(AnnotationTarget.TYPE,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
    vararg val layout: Int
)