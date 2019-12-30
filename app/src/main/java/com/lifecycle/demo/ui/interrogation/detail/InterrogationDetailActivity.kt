package com.lifecycle.demo.ui.interrogation.detail

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.databinding.ActivityInterrogationDetailBinding
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw

@Route(path = InterrogationDetailActivity.interrogationDetail)
@LayoutView(layout = [R.layout.activity_interrogation_detail])
class InterrogationDetailActivity : DataBindingActivity<InterrogationDetailModel, ActivityInterrogationDetailBinding>() {
        companion object{const val interrogationDetail = "${tomtaw}interrogationDetail"}
}