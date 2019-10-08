package com.customers.zktc.ui.home

import android.view.ViewGroup
import com.binding.model.inflate.inter.Item
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.ui.home.page.HomePageFragment

class HomeFragmentEntity :Item<BaseFragment<*>> {
    private var fragment:BaseFragment<*>?=null
    override fun getItem(position: Int, container: ViewGroup): BaseFragment<*> {
        when (position) {
            0 -> fragment = HomePageFragment()
            else -> fragment = HomePageFragment()
        }
        return fragment!!
    }
}