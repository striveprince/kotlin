package com.lifecycle.demo.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.inject.data.Api
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.viewmodel.LifeViewModel
import timber.log.Timber

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:23
 * Email: 1033144294@qq.com
 */
class HomeModel : LifeViewModel() {
    val newCount = MutableLiveData(0)
    val waitCount = MutableLiveData(0)
    val allCount = MutableLiveData(0)
    val fragmentState = Bundle()
    var beforeIndex = -1
    val currentIndex = MutableLiveData(0)
    private val homeEntities = arrayListOf(
        HomeEntity(R.id.home_interrogation),
        HomeEntity(R.id.home_browse),
        HomeEntity(R.id.home_message),
        HomeEntity(R.id.home_mine)
    )

    fun onNavigationItemSelected(selectedItemId: Int, position: Int) {
        Timber.i("onNavigationItemSelected:selectedItemId=%1d,position=%2d", selectedItemId, position)
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        (owner as HomeActivity).apply {
            currentIndex.observer(this) { checkFragment(it, supportFragmentManager) }
            bundle.restoreFragment()
        }
    }

    fun restoreFragmentState(savedInstanceState: Bundle) = savedInstanceState.restoreFragment()

    private fun Bundle?.restoreFragment() {
        currentIndex.value = this?.getBundle(HomeActivity.savedInstance)?.let {
            fragmentState.putAll(it)
            getInt(HomeActivity.fragmentIndex)
        } ?: 0
    }


//
//    override fun onTabReselected(p0: TabLayout.Tab?) {
//
//    }
//
//    override fun onTabUnselected(p0: TabLayout.Tab?) {
//
//    }
//
//    override fun onTabSelected(p0: TabLayout.Tab?) {
//        currentIndex.value = p0?.position
//    }

    private fun checkFragment(index: Int, fm: FragmentManager) {
        if (index < 0 || index == beforeIndex || index >= homeEntities.size) return
        val ft = fm.beginTransaction()
        if (index < beforeIndex) ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out)
        else ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out)
        if (beforeIndex >= 0) ft.hide(fm.getFragment(fragmentState, homeEntities[beforeIndex].route)!!)
        val route = homeEntities[index].route
        val fragment = ARouterUtil.getFragment(fm, route, fragmentState, homeEntities[index].bundle)
        if (!fragment.isAdded) ft.add(R.id.home_frame_layout, fragment)
        ft.show(fragment)
        ft.commitAllowingStateLoss()
        fm.putFragment(fragmentState, route, fragment)
        beforeIndex = index
    }

//    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
//        currentIndex.value = homeEntities.indexOf(HomeEntity(p0.itemId))
//        return true
//    }

}