package com.customers.zktc.inject.scope

import javax.inject.Scope

/**
 * Company: 中科同创
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