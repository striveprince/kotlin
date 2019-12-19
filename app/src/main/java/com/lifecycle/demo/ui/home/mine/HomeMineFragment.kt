package com.lifecycle.demo.ui.home.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.binding.DataBindingFragment
import com.lifecycle.demo.databinding.FragmentHomeMineBinding
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine
import com.lifecycle.binding.inter.bind.annotation.LayoutView

@Route(path = mine)
@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineFragment:DataBindingFragment<HomeMineModel, FragmentHomeMineBinding>() {
    companion object{ const val mine = FragmentComponent.Config.fragment+"mine" }
}