package com.lifecycle.binding.life.normal

import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.normal.Normal
import com.lifecycle.binding.life.BaseActivity

open class NormalActivity <Model:ViewModel>:BaseActivity<Model,Any>(),Normal<Model>{

}