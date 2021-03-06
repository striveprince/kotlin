package com.lifecycle.demo.ui.home.interrogation

import androidx.lifecycle.MutableLiveData
import com.lifecycle.demo.R
import com.lifecycle.rx.viewmodel.LifeViewModel

class HomeInterrogationModel: LifeViewModel()  {

    val position = MutableLiveData(0)
    val items = arrayListOf(
        HomeInterrogationEntity(R.string.all_interrogation),
        HomeInterrogationEntity(R.string.new_interrogation),
        HomeInterrogationEntity(R.string.wait_interrogation)
    )
//
//    override fun onPageScrollStateChanged(state: Int) {
//
//    }
//
//    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//    }
//
//    override fun onPageSelected(position: Int) {
//        this.position.value = position
//    }
//
//    override fun onTabReselected(p0: TabLayout.Tab?) {
//    }
//
//    override fun onTabUnselected(p0: TabLayout.Tab?) {
//    }
//
//    override fun onTabSelected(p0: TabLayout.Tab?) {
//        this.position.value = p0?.position
//    }
}