package com.customers.zktc.ui.home

import android.view.ViewGroup
import com.binding.model.inflate.inter.Item
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.ui.home.cart.HomeCartFragment
import com.customers.zktc.ui.home.classify.HomeClassifyFragment
import com.customers.zktc.ui.home.page.HomePageFragment

class HomeFragmentEntity : Item<BaseFragment<*>> {
    private var fragment: BaseFragment<*>? = null
    override fun getItem(position: Int, container: ViewGroup): BaseFragment<*> {
        if (fragment == null) {
            fragment = when (position) {
                1 -> HomeCartFragment()
                2 -> HomeClassifyFragment()
                3 -> HomeClassifyFragment()
                4 -> HomeClassifyFragment()
                else -> HomePageFragment()
            }
        }
        return fragment!!
    }
}