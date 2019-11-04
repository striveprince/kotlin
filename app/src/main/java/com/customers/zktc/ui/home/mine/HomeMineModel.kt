package com.customers.zktc.ui.home.mine

import android.os.Bundle
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.FragmentHomeMineBinding
import com.customers.zktc.inject.data.Api
import javax.inject.Inject

@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineModel @Inject constructor() : ViewModel<HomeMineFragment,FragmentHomeMineBinding>(){
    @Inject lateinit var api: Api
    override fun attachView(savedInstanceState: Bundle?, t: HomeMineFragment) {
        super.attachView(savedInstanceState, t)
        binding.bean = api.userBean()
    }
}
