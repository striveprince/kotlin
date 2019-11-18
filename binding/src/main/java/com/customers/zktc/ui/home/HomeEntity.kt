package com.customers.zktc.ui.home

import androidx.fragment.app.Fragment
import com.customers.zktc.R
import com.google.android.material.tabs.TabLayout

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 16:25
 * Email: 1033144294@qq.com
 */
class HomeEntity(p: Int) {
    fun tab(): TabLayout.Tab =
        TabLayout.Tab()

    val fragment: Fragment = Fragment()

    val name = when(p){
        1-> R.string.app_name
        2-> R.string.app_name
        3-> R.string.app_name
        4-> R.string.app_name
        else-> R.string.app_name
    }

    private val icon = when(p){
        1-> R.drawable.ic_launcher_background
        else -> R.drawable.ic_launcher_background
    }
//    val tab = TabLayout.Tab().setText(name).setIcon(icon)

}