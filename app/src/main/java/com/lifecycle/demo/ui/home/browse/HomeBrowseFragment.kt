package com.lifecycle.demo.ui.home.browse

import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.normal.NormalFragment
import com.lifecycle.demo.R
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.browse.HomeBrowseFragment.Companion.browse

@LayoutView(layout=[R.layout.activity_start])
@Route(path = browse)
class HomeBrowseFragment : NormalFragment<HomeBrowseModel>(){
    companion object{ const val  browse = "$home/browse" }
}