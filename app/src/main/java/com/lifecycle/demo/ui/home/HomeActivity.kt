package com.lifecycle.demo.ui.home

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.view.SwipeBackLayout
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.ActivityHomeBinding
import com.lifecycle.demo.ui.DemoApplication

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 9:51
 * Email: 1033144294@qq.com
 */
@LayoutView(R.layout.activity_home)
@Route(path = HomeActivity.home)
class HomeActivity : DataBindingActivity<HomeModel,ActivityHomeBinding>() {
    companion object {
        const val home = DemoApplication.tomtaw + "home"
        const val fragmentIndex = "route"
        const val savedInstance = "savedInstance"
    }

    override fun isSwipe() = SwipeBackLayout.FROM_NO

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(fragmentIndex, model.beforeIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        model.restoreFragmentState(savedInstanceState)
    }

}
