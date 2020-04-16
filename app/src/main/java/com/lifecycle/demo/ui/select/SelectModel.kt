package com.lifecycle.demo.ui.select

import androidx.lifecycle.MutableLiveData
import com.lifecycle.coroutines.viewmodel.LifeViewModel

class SelectModel:LifeViewModel() {
    val params = MutableLiveData(ExamParam())
    val position = MutableLiveData(0)
}