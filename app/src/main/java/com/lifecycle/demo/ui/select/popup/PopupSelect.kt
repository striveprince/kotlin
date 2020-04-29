package com.lifecycle.demo.ui.select.popup

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.IListAdapter
import com.lifecycle.binding.adapter.AdapterType
import com.lifecycle.binding.inter.MultiplexSelect
import com.lifecycle.binding.inter.SpanLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.BindingInflate
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.life.AppLifecycle.Companion.application
import com.lifecycle.demo.R
import java.util.*

open class PopupSelect(val type: String,val option: String="") : MultiplexSelect, SpanLookup {
    val checked = ObservableBoolean()
    override var checkWay: Int = 3
    lateinit var event: IEvent<Any>
    override fun event(event: IEvent<Any>) {
        this.event = event
    }

    fun onSelectClick(view: View) {
        event.setEvent(AdapterType.select, this, view = view)
    }

    fun time():Date{
        return when(option){
            application.getString(R.string.day_one)->Calendar.getInstance().apply { set(Calendar.DATE, get(Calendar.DATE)-1) }.time
            application.getString(R.string.week_one)-> Date(System.currentTimeMillis() - 86400 * 7 * 1000)
            application.getString(R.string.month_one)->  Calendar.getInstance().apply { set(Calendar.MONTH, get(Calendar.MONTH)-1) }.time
            application.getString(R.string.year_one)-> Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR)-1) }.time
            else->Date(0)
        }
    }
}

@LayoutView(layout = [R.layout.holder_home_option_title])
class SelectTitle(type: String) : PopupSelect(type), Inflate {
    override fun getSpanSize() = 4

    override fun View.binding() {
        findViewById<CheckBox>(R.id.select_title).setOnClickListener { onSelectClick(it) }
        findViewById<TextView>(R.id.title).text = type
    }
}


@LayoutView(layout = [R.layout.holder_select_option, R.layout.holder_select_option_date, R.layout.holder_select_option_normal])
class SelectOption(type: String, option: String, val value: String = option) : PopupSelect(type,option), BindingInflate<ViewDataBinding> {
    override fun getSpanSize() = 1
    override var checkWay: Int = 1

    override fun layoutIndex(): Int {
        return when ((event as IListAdapter).array[R.id.select_option]) {
            2 -> 1
            3 -> 2
            else -> 0
        }
    }

    override fun selectMax(): Int {
        return if (type == application.getString(R.string.type_date)) 1 else super.selectMax()
    }

    override fun selectType(): String {
        return type
    }

    override fun select(b: Boolean): Boolean {
        checked.set(b)
        return b
    }

    override fun isSelected(): Boolean {
        return checked.get()
    }

}