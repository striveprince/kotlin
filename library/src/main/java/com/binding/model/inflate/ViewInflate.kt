package com.binding.model.inflate

import androidx.databinding.ViewDataBinding
import com.binding.model.inflate.inter.Inflate

abstract class ViewInflate<Binding : ViewDataBinding> : ViewParse<Binding>(), Inflate<Binding> {

}