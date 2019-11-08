
package com.lifecycle.binding.annoation

import androidx.annotation.IntDef
import com.lifecycle.binding.inflate.obj.EventType.add
import com.lifecycle.binding.inflate.obj.EventType.click
import com.lifecycle.binding.inflate.obj.EventType.long
import com.lifecycle.binding.inflate.obj.EventType.move
import com.lifecycle.binding.inflate.obj.EventType.no
import com.lifecycle.binding.inflate.obj.EventType.refresh
import com.lifecycle.binding.inflate.obj.EventType.remove
import com.lifecycle.binding.inflate.obj.EventType.select
import com.lifecycle.binding.inflate.obj.EventType.set
import com.lifecycle.binding.inflate.obj.EventType.touch


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [no, add, refresh, remove, set, move, click, long, select, touch])
annotation class HandleEvent