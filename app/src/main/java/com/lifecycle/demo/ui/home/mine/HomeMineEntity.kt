package com.lifecycle.demo.ui.home.mine

import android.view.View
import androidx.databinding.ObservableBoolean
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.ViewSelectInflate
import com.lifecycle.demo.R


@LayoutView(R.layout.holder_home_mine)
data class HomeMineEntity (val name:String="arvin"):Select, ViewSelectInflate() {
    override var checkWay: Int = 3
    lateinit var event: IEvent<Any>
    var check  = ObservableBoolean(false)

    override fun event(event: IEvent<Any>) {
        this.event = event
    }

    fun onClick(view:View){
        event.setEvent(AdapterType.select,this)
    }

    override fun isSelected(): Boolean {
        return check.get()
    }

    override fun select(b: Boolean): Boolean {
        check.set(b)
        return check.get()
    }
}