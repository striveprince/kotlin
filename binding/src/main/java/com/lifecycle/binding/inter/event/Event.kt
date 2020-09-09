package com.lifecycle.binding.inter.event

import android.view.View

data class Event<E>(val type: Int,val  e: E,val position: Int = -1,val view: View? = null)