package com.customers.zktc.ui.home.page

import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomePageBinding
import javax.inject.Inject

//@LayoutView(layout=[R.layout.fragment_home_page])
@LayoutView(layout = [R.layout.activity_home])

class HomePageModel @Inject constructor() : ViewModel<HomePageFragment, FragmentHomePageBinding>(){

}