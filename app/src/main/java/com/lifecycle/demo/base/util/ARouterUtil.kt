package com.lifecycle.demo.base.util

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.binding.Constant
import com.lifecycle.binding.life.AppLifecycle
import com.lifecycle.binding.life.LifecycleInit
import com.lifecycle.demo.R
import com.lifecycle.demo.inject.data.preference.user.UserApi
import com.lifecycle.demo.ui.home.HomeActivity
import com.lifecycle.demo.ui.interrogation.detail.InterrogationDetailActivity
import com.lifecycle.demo.ui.user.sign.login.SignInActivity
import java.lang.StringBuilder
import kotlin.math.min

object ARouterUtil {

    private fun build(path: String, bundle: Bundle = Bundle()): Postcard {
        return ARouter.getInstance()
            .build(path)
            .withTransition(R.anim.push_right_in, R.anim.push_left_out)
            .with(bundle)
    }


    private fun LifecycleInit<*>.navigation(uri: Uri) {
        val currentPath = javaClass.getAnnotation(Route::class.java)?.path ?: ""
        uri.pathSegments?.apply {
            if (size == 2) ARouter.getInstance().build(uri).navigation()
            else if(currentPath == getSamePath(currentPath)) {
//                fragmentManager()
            }
        }
    }

    private fun List<String>.getSamePath(currentPath: String):String {
        currentPath.split("/").let {
            val stringBuilder = StringBuilder("/")
            for (i in 0..min(size, it.size)) {
                if (get(i) === it[i]) stringBuilder.append(it[i]).append("/")
                else break
            }
            return stringBuilder.toString()
        }
    }


    fun LifecycleInit<*>.navigation(path: String) {
//        return navigation(Uri.parse("${domainUrl}${path}"))
        return navigation(Uri.parse(path))
    }

    fun start() {
        val path = if (UserApi.isLogin) HomeActivity.home else SignInActivity.signIn
        build(path).navigation()
    }

    fun finish() {
        AppLifecycle.activity().finish()
    }

    fun home() {
        build(HomeActivity.home)
            .navigation()
    }

    private fun buildFragment(route: Uri): Fragment {
        return ARouter.getInstance().build(route).navigation() as Fragment
    }

    fun findFragmentByTag(fragmentManager: FragmentManager? = null, route: String): Fragment {
        return fragmentManager?.findFragmentByTag(route) ?: buildFragment(Uri.parse(route))
//        return fragmentManager?.findFragmentByTag(route) ?: buildFragment(Uri.parse("${domainUrl}$route"))
    }

    fun interrogationDetail(id: String) {
        build(InterrogationDetailActivity.interrogationDetail)
            .withString(Constant.id, id)
            .navigation()
    }

    fun signIn() {
        build(SignInActivity.signIn).navigation()
    }

}

