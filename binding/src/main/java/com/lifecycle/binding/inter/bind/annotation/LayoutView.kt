package com.lifecycle.binding.inter.bind.annotation

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:30
 * Email: 1033144294@qq.com
 */
@Target(AnnotationTarget.TYPE,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutView(
    vararg val layout: Int
)