package com.customers.zktc.ui.home.member

import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.Container
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeMemberBinding
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_member])
class HomeMemberModel @Inject constructor(): ViewModel<CycleContainer<*>, FragmentHomeMemberBinding>(){

}
