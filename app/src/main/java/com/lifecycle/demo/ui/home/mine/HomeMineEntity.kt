package com.lifecycle.demo.ui.home.mine

import android.view.View
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.ViewBindInflate
import com.lifecycle.demo.R


@LayoutView(R.layout.holder_home_mine)
data class HomeMineEntity (val name:String="arvin"):Select, ViewBindInflate() {
    override var checkWay: Int = 3
    override var isChecked: Boolean = false
    lateinit var event:IEvent<Any>

    override fun event(event: IEvent<Any>) {
        this.event = event
    }

    fun onClick(view:View){
        event.setEvent(-1,this,AdapterType.select,view)
    }
}