package com.lifecycle.demo.ui.interrogation.detail

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.databinding.ActivityInterrogationDetailBinding
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.binding.inter.bind.annotation.LayoutView

@Route(path = InterrogationDetailActivity.interrogation_detail)
@LayoutView(layout = [R.layout.activity_interrogation_detail])
class InterrogationDetailActivity : DataBindingActivity<InterrogationDetailModel, ActivityInterrogationDetailBinding>() {
        companion object{const val interrogation_detail = "${ActivityComponent.Config.tomtaw}interrogation_detail"}
}