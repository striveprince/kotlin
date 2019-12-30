package com.lifecycle.demo.ui.home.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.demo.databinding.FragmentHomeMineBinding
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home

@Route(path = mine)
@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineFragment: DataBindingFragment<HomeMineModel, FragmentHomeMineBinding>() {
    companion object{ const val mine = "$home/mine" }
}