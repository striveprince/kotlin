package com.lifecycle.demo.ui.select.popup

import android.view.View
import androidx.databinding.ObservableBoolean
import com.lifecycle.demo.R
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.AdapterType.select
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.SpanLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.ViewBindInflate


@LayoutView(layout = [R.layout.holder_home_option, R.layout.holder_home_option_title])
class SelectOption(val option: String, val type: String = option) : Select, ViewBindInflate(), SpanLookup {
    override var checkWay: Int = 3
    init { layoutIndex = if (option == type) 1 else 0 }
    private lateinit var event: IEvent<Any>
    val checked = ObservableBoolean()
    override fun event(event: IEvent<Any>) {
        this.event = event
    }

    override fun getSpanSize() = if (layoutIndex == 1) 1 else 4

    fun onSelectClick(view:View){
        event.setEvent(select,this,view = view)
    }

    override fun select(b: Boolean): Boolean {
        checked.set(b)
        return b
    }

    override fun isSelected(): Boolean {
        return checked.get()
    }

}