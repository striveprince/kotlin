package com.lifecycle.binding.life

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.util.*

interface LifecycleInit <Model>:Init,LifecycleOwner{
    fun initModel():Model
    fun owner():LifecycleOwner = this
    fun requireActivity(): FragmentActivity
    fun inject(savedInstanceState: Bundle?):View

    fun Int.stringRes(vararg any: Any)= requireActivity().string(this,*any)
    fun Int.drawable() = requireActivity().drawable(this)
    fun Int.color() = requireActivity().color(this)
}