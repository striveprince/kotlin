package com.lifecycle.demo.ui.home.message

import com.lifecycle.demo.R
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.DiffInflate
import com.lifecycle.binding.inter.inflate.Recycler
import com.lifecycle.binding.inter.inflate.RecyclerBindInflate

@LayoutView(layout = [R.layout.holder_home_message])
class HomeMessageEntity:DiffInflate,RecyclerBindInflate,Recycler{

}