package com.lifecycle.demo.ui.select.popup

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.PopupSelectBinding
import com.lifecycle.binding.inter.ISelectList
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.coroutines.inflate.ListViewInflate


@LayoutView(layout = [R.layout.popup_select])
class PopupRecyclerInflate<E : Select>(
    private val layoutManager: RecyclerView.LayoutManager,
    private val selectAdapter: ISelectList<E>,
    private val block:(List<E>)->Unit = {}
) : ListViewInflate<E, PopupSelectBinding>(selectAdapter) {

    override fun initBinding(t: PopupSelectBinding) {
        super.initBinding(t)
//        t.swipeBackLayout.recyclerView.layoutManager = layoutManager
    }

    fun onResetClick(v: View) {
        selectAdapter.selectList()
    }

    fun onConfirmClick(v: View) {
        block(selectAdapter.selectList)
    }
}