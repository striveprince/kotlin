package com.lifecycle.demo.ui.home.interrogation

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.lifecycle.demo.R
import com.lifecycle.binding.viewmodel.LifeViewModel
import com.lifecycle.demo.inject.data.Api
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.App
import com.lifecycle.binding.util.observer

class HomeInterrogationModel:LifeViewModel() ,ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    val position = MutableLiveData(0)
    val items = arrayListOf(
        HomeInterrogationEntity(R.string.all_interrogation),
        HomeInterrogationEntity(R.string.new_interrogation),
        HomeInterrogationEntity(R.string.wait_interrogation)
    )

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        this.position.value = position
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        this.position.value = p0?.position
    }
}