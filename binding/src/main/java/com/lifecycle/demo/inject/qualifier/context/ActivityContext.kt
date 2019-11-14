package com.lifecycle.demo.inject.qualifier.context

import javax.inject.Qualifier
import kotlin.annotation.Retention

/**
 * project：zktc
 * description：
 * create developer： admin
 * create time：15:25
 * modify developer：  admin
 * modify time：15:25
 * modify remark：
 *
 * @version 1.0
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext
