package com.lifecycle.coroutines.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun launchMain(context: CoroutineContext = EmptyCoroutineContext,
               start: CoroutineStart = CoroutineStart.DEFAULT,
               block: suspend CoroutineScope.() -> Unit):Job {
    return CoroutineScope(Dispatchers.Main).launch(context, start, block)
}

fun launchDefault(context: CoroutineContext = EmptyCoroutineContext,
                  start: CoroutineStart = CoroutineStart.DEFAULT,
                  block: suspend CoroutineScope.() -> Unit):Job {
    return CoroutineScope(Dispatchers.Default).launch(context, start, block)
}

fun launchIo(context: CoroutineContext = EmptyCoroutineContext,
             start: CoroutineStart = CoroutineStart.DEFAULT,
             block: suspend CoroutineScope.() -> Unit):Job {
    return CoroutineScope(Dispatchers.IO).launch(context, start, block)
}