package com.customers.zktc.ui.home

import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.ActivityHomeBinding
import javax.inject.Inject

@LayoutView(layout = [R.layout.activity_home])
class HomeModel @Inject constructor() : ViewModel<HomeActivity, ActivityHomeBinding>() {
}