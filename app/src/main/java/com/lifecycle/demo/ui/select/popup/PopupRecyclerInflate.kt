package com.lifecycle.demo.ui.select.popup

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.inter.ISelectMultiplexList
import com.lifecycle.binding.inter.MultiplexSelect
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.PopupSelectBinding
import com.lifecycle.rx.inflate.ListViewInflate

@LayoutView(layout = [R.layout.popup_select])
class PopupRecyclerInflate<E : MultiplexSelect>(
    private val layoutManager: RecyclerView.LayoutManager,
    private val selectAdapter: ISelectMultiplexList<E>,
    private val block:(List<E>)->Unit = {}
) : ListViewInflate<E, PopupSelectBinding>(selectAdapter) {

    override fun initBinding(context: Context, t: PopupSelectBinding) {
        super.initBinding(context,t)
        t.recyclerView.layoutManager = layoutManager
        t.recyclerView.layoutAnimation = null
        t.recyclerView.animation = null
        t.smartRefreshLayout.setEnableLoadMore(false)
    }

    fun onResetClick(v: View) {
        selectAdapter.selectList()
    }

    fun onConfirmClick(v: View) {
        block(selectAdapter.selectList)
    }

}