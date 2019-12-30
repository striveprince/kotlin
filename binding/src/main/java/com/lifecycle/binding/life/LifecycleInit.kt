package com.lifecycle.binding.life

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.util.*

interface LifecycleInit <Model>:Init,LifecycleOwner{
    fun initModel():Model
    fun owner():LifecycleOwner = this
    fun getActivity(): FragmentActivity?
    fun fragmentManager():FragmentManager
    fun inject(savedInstanceState: Bundle?):View

    fun Int.stringRes(vararg any: Any)= getActivity()!!.string(this,*any)
    fun Int.drawable() = getActivity()!!.drawable(this)
    fun Int.color() = getActivity()!!.color(this)

    fun Float.floatToPx() = getActivity()!!.floatToPx(this)
    fun Float.floatToDp() = getActivity()!!.floatToDp(this)
    fun toPx(dp: Int): Int = (dp.toFloat().floatToPx() + 0.5).toInt()
    fun toDp(px: Int) = (px.toFloat().floatToDp() + 0.5).toInt()
    fun screenWidth() = getActivity()!!.screenWidth()
    fun screenHeight() = getActivity()!!.screenHeight()
}