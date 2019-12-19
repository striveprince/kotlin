package com.lifecycle.binding.adapter.databinding.inter

import androidx.recyclerview.widget.RecyclerView

interface StateChangedListener {
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
}