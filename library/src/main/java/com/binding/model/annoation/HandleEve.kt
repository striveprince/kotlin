
package com.binding.model.annoation

import androidx.annotation.IntDef
import com.binding.model.inflate.obj.EventType.add
import com.binding.model.inflate.obj.EventType.click
import com.binding.model.inflate.obj.EventType.long
import com.binding.model.inflate.obj.EventType.move
import com.binding.model.inflate.obj.EventType.no
import com.binding.model.inflate.obj.EventType.refresh
import com.binding.model.inflate.obj.EventType.remove
import com.binding.model.inflate.obj.EventType.select
import com.binding.model.inflate.obj.EventType.set
import com.binding.model.inflate.obj.EventType.touch


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [no, add, refresh, remove, set, move, click, long, select, touch])
annotation class HandleEve