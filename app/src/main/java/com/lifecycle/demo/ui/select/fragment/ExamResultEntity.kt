package com.lifecycle.demo.ui.select.fragment

import androidx.databinding.ViewDataBinding
import com.lifecycle.demo.R
import com.lifecycle.binding.inter.bind.DataBeanInflate
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.bind.data.DataBindInflate
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.demo.inject.bean.ExamResultBean

@LayoutView(layout=[R.layout.holder_home_list])
class ExamResultEntity(override val bean: ExamResultBean):Diff, DataBindInflate<ExamResultBean,ViewDataBinding> {
}