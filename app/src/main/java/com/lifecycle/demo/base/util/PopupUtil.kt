package com.lifecycle.demo.base.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewInflateBinding
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.ListInflate
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.rx.inflate.list.ListViewInflate


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

fun LifecycleInit<*>.popup(inflate: Inflate, block: PopupWindow.() -> Unit = {}): PopupWindow {
    return inflate.run {
        PopupWindow(createView(requireActivity())).also {
            it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            it.isOutsideTouchable = true
            it.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT

            block(it)
        }
    }
}
