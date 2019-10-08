package com.customers.zktc.ui.home

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.binding.model.adapter.databinding.TabLayoutBindingAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.inter.Item
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.databinding.ActivityHomeBinding
import com.customers.zktc.inject.qualifier.manager.ActivityFragmentManager
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import javax.inject.Inject

@LayoutView(layout = [R.layout.activity_home])
class HomeModel
@Inject constructor(@ActivityFragmentManager private val fragmentManager: FragmentManager)
    : ViewModel<HomeActivity, ActivityHomeBinding>(),
        TabLayout.OnTabSelectedListener {
    private var currentPosition = 1
    private val fragments = ArrayList<Item<BaseFragment<*>>>()
    override fun attachView(savedInstanceState: Bundle?, t: HomeActivity) {
        super.attachView(savedInstanceState, t)
        addDisposables(
            Observable.range(0,5)
                .map { HomeFragmentEntity() }
                .toList()
                .map { fragments.addAll(it) }
                .subscribe()
        )
        binding!!.tabLayout.addOnTabSelectedListener(this)
        checkTab(0)
    }

    private fun checkFragment(position: Int) {
        if (position < 0 || position >= fragments.size || position == currentPosition) return
        val ft = fragmentManager.beginTransaction()
        if (currentPosition >= 0) {
            val beforeFragment = fragments[currentPosition].getItem(position, binding!!.frameLayout)
            ft.hide(beforeFragment)
        }
        val fragment = fragments[position].getItem(position, binding!!.frameLayout)
        if (!fragment.isAdded) ft.add(R.id.frameLayout, fragment)
        ft.show(fragment)
        ft.commitAllowingStateLoss()
        currentPosition = position
    }


    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val p = tab!!.position
        if(p==3)checkTab(currentPosition)
        else checkFragment(p)
    }

    private fun checkTab(currentPosition: Int) {
        TabLayoutBindingAdapter.setScrollPosition(binding!!.tabLayout,currentPosition)
//        ArouterUtil.login()
    }
}