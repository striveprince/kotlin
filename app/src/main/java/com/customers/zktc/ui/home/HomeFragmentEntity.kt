package com.customers.zktc.ui.home

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.binding.model.inflate.inter.Item
import com.customers.zktc.R
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.ui.home.cart.HomeCartFragment
import com.customers.zktc.ui.home.classify.HomeClassifyFragment
import com.customers.zktc.ui.home.member.HomeMemberFragment
import com.customers.zktc.ui.home.mine.HomeMineFragment
import com.customers.zktc.ui.home.page.HomePageFragment
import com.google.android.material.tabs.TabLayout

class HomeFragmentEntity(val p: Int) : Item<BaseFragment<*>> {
    val fragment: BaseFragment<*> = when (p) {
        1 -> HomeClassifyFragment()
        2 -> HomeMemberFragment()
        3 -> HomeCartFragment()
        4 -> HomeMineFragment()
        else -> HomePageFragment()
    }

    val name = when(p){
        1-> R.string.cap_home_main
        2-> R.string.cap_home_main
        3-> R.string.cap_home_main
        4-> R.string.cap_home_main
        else-> R.string.cap_home_main
    }
    private val icon = when(p){
        1->R.drawable.ic_launcher_background
        else ->R.drawable.ic_launcher_background
    }
    val tab = TabLayout.Tab().setText(name).setIcon(icon)

    override fun getItem(position: Int, container: ViewGroup): BaseFragment<*> {
        return fragment
    }
}