package com.lifecycle.demo.base.life

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.base.container.DataBindingActivity
import javax.inject.Inject

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/13 17:13
 * Email: 1033144294@qq.com
 */
abstract class BaseActivity<Model: ViewModel,Binding : ViewDataBinding> :DataBindingActivity<Model,Binding>(){

    @Inject lateinit var model:Model


}