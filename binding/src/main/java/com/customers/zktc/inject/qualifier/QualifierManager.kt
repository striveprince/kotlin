package com.customers.zktc.inject.qualifier

import javax.inject.Qualifier

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 12:00
 * Email: 1033144294@qq.com
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityFragmentManager
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ChildFragmentManager
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SupportFragmentManager