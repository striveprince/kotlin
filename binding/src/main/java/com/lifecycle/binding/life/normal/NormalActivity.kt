package com.lifecycle.binding.life.normal

import android.view.View
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.normal.Normal
import com.lifecycle.binding.life.BaseActivity

open class NormalActivity <Model:ViewModel>:BaseActivity<Model, View>(),Normal<Model>{

}