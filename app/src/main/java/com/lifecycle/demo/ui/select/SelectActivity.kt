package com.lifecycle.demo.ui.select

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.normal.NormalActivity
import com.lifecycle.binding.view.SwipeBackLayout
import com.lifecycle.demo.base.util.*
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.select.SelectActivity.Companion.select
import com.lifecycle.demo.ui.select.consult.HomeConsultFragment.Companion.homeConsult

@Route(path = select)
@LayoutView(layout = [R.layout.activity_select])
class SelectActivity: NormalActivity<SelectModel>() {
    companion object { const val select = tomtaw + "select" }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        showFragment(supportFragmentManager,homeConsult)
    }

    override fun isSwipe() = SwipeBackLayout.FROM_NO

    private fun showFragment(fm: FragmentManager, route: String) {
        fm.beginTransaction().run {
            val fragment = ARouterUtil.findFragmentByTag(fm, route)
            if (!fragment.isAdded) add(R.id.home_frame_layout, fragment, route)
            show(fragment)
            commitAllowingStateLoss()
        }
    }
}