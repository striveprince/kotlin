package com.lifecycle.binding.life.normal

import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.normal.Normal
import com.lifecycle.binding.life.BaseFragment

open class NormalFragment <Model:ViewModel>:BaseFragment<Model,Any>(),Normal<Model>{

}