package com.lifecycle.binding.annoation

import androidx.lifecycle.ViewModel


@Target(AnnotationTarget.TYPE,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
    vararg val layout: Int
)