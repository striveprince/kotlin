package com.lifecycle.demo.ui.home.interrogation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lifecycle.binding.inter.inflate.Item
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment.Companion.interrogationList

class HomeInterrogationEntity(strRes: Int) : Item {
    private val params = when (strRes) {
        R.string.new_interrogation -> "new"
        R.string.wait_interrogation -> "wait"
        else -> "all"
    }
//    private val paramsBundle = bundleOf(Constant.params to params)
    override fun fragment(fm: FragmentManager): Fragment {
        return ARouterUtil.findFragmentByTag(fm,"${interrogationList}?params=$params")
    }
}