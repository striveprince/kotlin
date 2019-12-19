package com.lifecycle.binding.util

import android.view.ViewManager
import androidx.viewpager2.widget.ViewPager2
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design._TabLayout

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 14:35
 * Email: 1033144294@qq.com
 */
inline fun ViewManager.viewPager2(init: ViewPager2.() -> Unit): ViewPager2 =
    ankoView({ ViewPager2(it) }, theme = 0, init = init)
//inline fun ViewManager.bottomNavigationView(init: BottomNavigationView.() -> Unit): BottomNavigationView =
//    ankoView({ BottomNavigationView(it) }, theme = 0, init = init)
//
//inline fun ViewManager.recyclerView(init: RecyclerView.() -> Unit): RecyclerView =
//    ankoView({ RecyclerView(it) }, theme = 0, init = init)

