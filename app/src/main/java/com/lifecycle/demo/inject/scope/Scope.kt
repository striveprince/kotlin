package com.lifecycle.demo.inject.scope

import javax.inject.Scope

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 12:01
 * Email: 1033144294@qq.com
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ServiceScope
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope