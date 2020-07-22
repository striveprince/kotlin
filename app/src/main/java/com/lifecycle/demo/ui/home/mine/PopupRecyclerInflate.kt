package com.lifecycle.demo.ui.home.mine

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecycle.binding.inter.event.IListAdapter
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewInflateBinding
import com.lifecycle.coroutines.inflate.ListViewInflate


@LayoutView(layout = [R.layout.layout_swipe_recycler_view_inflate])
class PopupRecyclerInflate<E : Select>(val context: Context, adapter: IListAdapter<E>) : ListViewInflate<E, LayoutSwipeRecyclerViewInflateBinding>(adapter) {
    override fun initBinding(context: Context,t: LayoutSwipeRecyclerViewInflateBinding) {
        super.initBinding(context,t)
        t.recyclerView.layoutManager = LinearLayoutManager(context)
    }

}