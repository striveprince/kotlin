package com.lifecycle.demo.ui.home.message

import com.lifecycle.demo.R
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.inter.inflate.Recycler
import com.lifecycle.binding.inter.inflate.ViewBindInflate

@LayoutView(layout = [R.layout.holder_home_message])
class HomeMessageEntity:Diff,ViewBindInflate,Recycler{

}