package com.lifecycle.demo.ui.home.mine

import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.RecyclerBindInflate
import com.lifecycle.demo.R


@LayoutView(R.layout.holder_home_mine)
data class HomeMineEntity (val name:String="arvin"):Select, RecyclerBindInflate {
    override var checkWay: Int = 3
    override var isChecked: Boolean = false
}