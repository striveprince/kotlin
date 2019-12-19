package com.lifecycle.demo.ui.home.interrogation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment
import com.lifecycle.binding.Constant
import com.lifecycle.binding.inter.inflate.Item

class HomeInterrogationEntity(strRes: Int) : Item {
    private val params = when (strRes) {
        R.string.new_interrogation -> "new"
        R.string.wait_interrogation -> "wait"
        else -> "all"
    }
    private val paramsBundle = bundleOf(Constant.params to params)
    override fun fragment(fm: FragmentManager, bundle: Bundle): Fragment {
//        you could use two way to get fragment one is getFragment and the other is  findFragmentByTag function
//        return ARouterUtil.findFragmentByTag(fm,InterrogationListFragment.interrogationList,paramsBundle,params)
        return ARouterUtil.getFragment(fm, InterrogationListFragment.interrogationList, bundle, paramsBundle, params)
    }
}