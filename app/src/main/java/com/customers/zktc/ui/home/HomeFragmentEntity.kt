package com.customers.zktc.ui.home

import android.view.ViewGroup
import com.binding.model.inflate.inter.Item
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.ui.home.cart.HomeCartFragment
import com.customers.zktc.ui.home.classify.HomeClassifyFragment
import com.customers.zktc.ui.home.member.HomeMemberFragment
import com.customers.zktc.ui.home.mine.HomeMineFragment
import com.customers.zktc.ui.home.page.HomePageFragment

class HomeFragmentEntity : Item<BaseFragment<*>> {
    private var fragment: BaseFragment<*>? = null
    override fun getItem(position: Int, container: ViewGroup): BaseFragment<*> {
        if (fragment == null) {
            fragment = when (position) {
                1 -> HomeClassifyFragment()
                2 -> HomeMemberFragment()
                3 -> HomeCartFragment()
                4 -> HomeMineFragment()
                else -> HomePageFragment()
            }
        }
        return fragment!!
    }
}