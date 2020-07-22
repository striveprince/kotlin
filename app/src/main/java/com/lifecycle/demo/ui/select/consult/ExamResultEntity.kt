package com.lifecycle.demo.ui.select.consult

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inter.event.IEvent
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.bind.data.DataBindInflate
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.bean.ExamResultBean

@LayoutView(layout = [R.layout.holder_home_list, R.layout.holder_home_list_grid])
class ExamResultEntity(override val bean: ExamResultBean) : Diff, DataBindInflate<ExamResultBean, ViewDataBinding> {
    var event: IListAdapter<*>? = null
    override fun event(event: IEvent<Any>) {
        if (event is IListAdapter)
            this.event = event
    }

    override fun layoutIndex(): Int {
        return if(event?.array?.get(R.layout.holder_home_list) == 0) 0 else 1
    }
}