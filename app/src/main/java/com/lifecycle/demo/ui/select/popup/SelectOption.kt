package com.lifecycle.demo.ui.select.popup

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.MultiplexSelect
import com.lifecycle.binding.inter.SpanLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.demo.R


open class PopupSelect(val type: String) : MultiplexSelect, SpanLookup {
    val checked = ObservableBoolean()
    override var checkWay: Int = 3
    lateinit var event: IEvent<Any>
    override fun event(event: IEvent<Any>) {
        this.event = event
    }

    fun onSelectClick(view: View) {
//        if (view is CheckBox) checked.set(view.isChecked)
        event.setEvent(AdapterType.select, this, view = view)
    }
}

@LayoutView(layout = [R.layout.holder_home_option_title])
class SelectTitle(type: String) : PopupSelect(type), Inflate {
    override fun getSpanSize() = 4

    override fun View.binding() {
        findViewById<CheckBox>(R.id.select_title).setOnClickListener { onSelectClick(it) }
        findViewById<TextView>(R.id.select_title).text = type
    }
}


@LayoutView(layout = [R.layout.holder_select_option,R.layout.holder_select_option_date,R.layout.holder_select_option_normal])
class SelectOption(val option: String, type: String = option) : PopupSelect(type), BindingInflate<ViewDataBinding> {
    override fun getSpanSize() = 1
    var date = ""

    override fun View.binding() {
    }

    override fun selectType(): String {
        return if(date.isEmpty())super.selectType()else date
    }

    override var checkWay: Int = 1

    override fun selectMax(): Int {
        return if(date.isEmpty())super.selectMax()else 1
    }
    override fun select(b: Boolean): Boolean {
        checked.set(b)
        return b
    }

    override fun isSelected(): Boolean {
        return checked.get()
    }

}