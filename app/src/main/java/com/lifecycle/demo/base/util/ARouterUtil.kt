package com.lifecycle.demo.base.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.data.preference.user.UserApi
import com.lifecycle.demo.ui.home.HomeActivity
import com.lifecycle.demo.ui.interrogation.detail.InterrogationDetailActivity
import com.lifecycle.demo.ui.user.sign.login.SignInActivity
import com.lifecycle.demo.ui.video.VideoActivity
import com.lifecycle.binding.App
import com.lifecycle.binding.Constant
import com.lifecycle.binding.base.rotate.TimeUtil

object ARouterUtil {

    private fun build(path: String, bundle: Bundle = Bundle()): Postcard {
        return ARouter.getInstance()
            .build(path)
            .withTransition(R.anim.push_right_in, R.anim.push_left_out)
            .with(bundle)
    }

    fun start() {
        val path = if(UserApi.isLogin)HomeActivity.home else SignInActivity.signIn
        build(path).navigation()
    }

    fun finish() {
        App.activity().finish()
    }

    fun home() {
        build(HomeActivity.home)
            .navigation()
        finish()
    }

    private fun buildFragment(route: String,bundle: Bundle = Bundle()): Fragment {
        return ARouter.getInstance().build(route).with(bundle).navigation() as Fragment
    }

    fun findFragmentByTag(fragmentManager: FragmentManager? = null,route: String, params:Bundle= Bundle(), tag:String = route): Fragment {
        return fragmentManager?.findFragmentByTag(tag) ?: buildFragment(route,params)
    }

    fun getFragment(fm: FragmentManager, route: String, fragmentState: Bundle, params:Bundle= Bundle(), tag:String = route): Fragment {
        return fm.getFragment(fragmentState, tag)?: buildFragment(route,params)
    }

    fun interrogationDetail(id: String) {
        build(InterrogationDetailActivity.interrogation_detail)
            .withString(Constant.id,id)
            .navigation()
    }

}