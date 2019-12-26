package com.lifecycle.binding.life

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.util.string

interface LifecycleInit <Model>:Init,LifecycleOwner{
    fun initModel():Model
    fun owner():LifecycleOwner = this
    fun getActivity(): FragmentActivity?
    fun inject(savedInstanceState: Bundle?):View
    fun Int.stringRes(vararg any: Any)= getActivity()!!.string(this,*any)
    fun Int.drawable() = ContextCompat.getDrawable(getActivity()!!,this)
    fun Int.color() = ContextCompat.getColor(getActivity()!!,this)
    fun Float.floatToPx() = getActivity()!!.resources.displayMetrics.density * this
    fun Float.floatToDp() = this /  getActivity()!!.resources.displayMetrics.density
    fun toPx(dp: Int): Int = (dp.toFloat().floatToPx() + 0.5).toInt()
    fun toDp(px: Int) = (px.toFloat().floatToDp() + 0.5).toInt()
    fun screenWidth() = getActivity()!!.resources.displayMetrics.widthPixels
    fun screenHeight() = getActivity()!!.resources.displayMetrics.heightPixels

}