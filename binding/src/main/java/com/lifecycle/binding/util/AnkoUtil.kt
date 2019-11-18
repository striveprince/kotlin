package com.lifecycle.binding.util

import android.view.ViewManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.custom.ankoView

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 14:35
 * Email: 1033144294@qq.com
 */
inline fun ViewManager.tabLayout(init:TabLayout.()->Unit):TabLayout =
    ankoView({TabLayout(it) },theme = 0,init = init)

inline fun ViewManager.bottomNavigationView(init:BottomNavigationView.()->Unit):BottomNavigationView =
    ankoView({BottomNavigationView(it)},theme = 0,init = init)