package com.lifecycle.demo.ui.select

import androidx.lifecycle.MutableLiveData
import com.lifecycle.coroutines.viewmodel.LifeViewModel
import com.lifecycle.demo.R

class SelectModel:LifeViewModel() {
    val params = MutableLiveData(ExamParam())
    val position = MutableLiveData(0)

}