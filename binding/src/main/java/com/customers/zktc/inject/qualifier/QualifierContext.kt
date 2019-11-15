package com.customers.zktc.inject.qualifier

import javax.inject.Qualifier

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 12:00
 * Email: 1033144294@qq.com
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class FragmentContext
