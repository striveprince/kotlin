package com.lifecycle.demo.ui.home

import android.os.Bundle
import com.lifecycle.demo.R
import com.lifecycle.demo.ui.home.browse.HomeBrowseFragment.Companion.browse
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation
import com.lifecycle.demo.ui.home.message.HomeMessageFragment.Companion.message
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 16:25
 * Email: 1033144294@qq.com
 */
data class HomeEntity(val id: Int) {
    val bundle: Bundle = Bundle()
    val route = when(id){
        R.id.home_interrogation->interrogation
        R.id.home_browse->browse
        R.id.home_message->message
        R.id.home_mine->mine
        else->mine
    }

//    val name: Int = when (id) {
//        R.id.home_interrogation->R.string.interrogation
//        R.id.home_browse->R.string.browse
//        R.id.home_message->R.string.message
//        else->R.string.mine
//    }
//    val icon: Int = when (id){
//        R.id.home_interrogation->R.drawable.inquiry
//        R.id.home_browse->R.drawable.retrieval
//        R.id.home_message->R.mipmap.ic_launcher
//        else->R.mipmap.ic_launcher
//    }
//    val fragment: Fragment = when(id) {
//        R.id.home_interrogation->HomeInterrogationFragment()
//        R.id.home_browse->HomeBrowseFragment()
//        R.id.home_message->HomeMessageFragment()
//        else ->HomeMineFragment()
//    }



}