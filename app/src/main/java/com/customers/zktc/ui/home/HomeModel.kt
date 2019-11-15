package com.customers.zktc.ui.home

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.binding.model.adapter.databinding.TabLayoutBindingAdapter
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.inter.Item
import com.binding.model.inflate.model.ViewModel
import com.binding.model.installApkFile
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.arouter.ARouterUtil
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.databinding.ActivityHomeBinding
import com.customers.zktc.inject.data.Api
import com.customers.zktc.inject.qualifier.manager.ActivityFragmentManager
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import javax.inject.Inject

@LayoutView(layout = [R.layout.activity_home])
class HomeModel
@Inject constructor(@ActivityFragmentManager private val fragmentManager: FragmentManager) :
    ViewModel<HomeActivity, ActivityHomeBinding>(),
    TabLayout.OnTabSelectedListener {
    @Inject lateinit var api: Api
    private var currentPosition = 1
    private val fragments = ArrayList<Item<BaseFragment<*>>>()


    override fun attachView(savedInstanceState: Bundle?, t: HomeActivity) {
        super.attachView(savedInstanceState, t)
        initFragment()
        checkUpdate(t)
    }

    private fun checkUpdate(t: HomeActivity) {
        api.checkUpdate(t)
            .subscribeNormal(t, { installApkFile(t, it) })
    }

    private fun initFragment() {
        Observable.range(0, 5)
            .map { HomeFragmentEntity(it) }
            .toList()
            .map { fragments.addAll(it) }
            .doOnSuccess { binding.tabLayout.addOnTabSelectedListener(this) }
            .toFlowable()
            .subscribeNormal({ checkTab(0) })
    }


    private fun checkFragment(position: Int) {
        if (position < 0 || position >= fragments.size || position == currentPosition) return
        val ft = fragmentManager.beginTransaction()
        if (currentPosition >= 0) {
            val beforeFragment = fragments[currentPosition].getItem(position, binding.frameLayout)
            ft.hide(beforeFragment)
        }
        val fragment = fragments[position].getItem(position, binding.frameLayout)
        if (!fragment.isAdded) ft.add(R.id.frameLayout, fragment)
        ft.show(fragment)
        ft.commitAllowingStateLoss()
        currentPosition = position
    }


    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        api.selectHomeTab(currentPosition, tab!!.position) { checkLogin(it) }
            .subscribeNormal({ checkFragment(it) })
    }

    private fun checkLogin(it: Int) {
        ARouterUtil.login()
        checkTab(it)
    }

    private fun checkTab(position: Int) {
        checkFragment(position)
        binding.tabLayout.let { TabLayoutBindingAdapter.setScrollPosition(it, position) }
    }


}