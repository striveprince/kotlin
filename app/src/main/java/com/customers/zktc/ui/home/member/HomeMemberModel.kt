package com.customers.zktc.ui.home.member

import android.os.Bundle
import android.view.View
import com.binding.model.annoation.LayoutView
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeMemberBinding
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_member])
class HomeMemberModel @Inject constructor(): ViewModel<CycleContainer<*>, FragmentHomeMemberBinding>(){
    val block :(View)->Unit = {}

    override fun attachView(savedInstanceState: Bundle?, t: CycleContainer<*>) {
        super.attachView(savedInstanceState, t)

    }
}
