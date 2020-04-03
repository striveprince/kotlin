package com.lifecycle.demo.base.util

import android.graphics.drawable.ColorDrawable
import android.widget.PopupWindow
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.LifecycleInit


/**
 *
 * @ProjectName:    kotlin
 * @Package:        com.lifecycle.demo.base.util
 * @ClassName:      PopupUtil
 * @Description:
 * @Author:         A
 * @CreateDate:     2020/4/3 15:02
 * @UpdateUser:     A
 * @UpdateDate:     2020/4/3 15:02
 * @UpdateRemark:
 * @Version:
 */

fun LifecycleInit<*>.popup(inflate: Inflate,block:PopupWindow.()->Unit = {}): PopupWindow {
    return PopupWindow(inflate.createView(requireActivity())).also {
        it.setBackgroundDrawable(ColorDrawable())
        it.isOutsideTouchable = true
        block(it)
    }
}
