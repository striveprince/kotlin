package com.lifecycle.binding.adapter.databinding.inter

import androidx.recyclerview.widget.RecyclerView

interface OnScrolledListener {
    fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
}