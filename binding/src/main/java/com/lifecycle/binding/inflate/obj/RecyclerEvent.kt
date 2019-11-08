package com.lifecycle.binding.inflate.obj

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [
    RecyclerStatus.loadBottom,
    RecyclerStatus.loadTop,
    RecyclerStatus.init,
    RecyclerStatus.resumeError,
    RecyclerStatus.click
])
annotation class RecyclerEvent{
}