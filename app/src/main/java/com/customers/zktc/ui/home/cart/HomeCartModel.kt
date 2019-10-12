package com.customers.zktc.ui.home.cart

import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.Container
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeCartBinding
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_cart],event = true)
class HomeCartModel @Inject constructor(): ViewModel<Container, FragmentHomeCartBinding>(){


}
