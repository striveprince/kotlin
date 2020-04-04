package com.lifecycle.demo.ui.home.mine

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewInflateBinding
import com.lifecycle.binding.inter.Select
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.demo.R
import com.lifecycle.rx.inflate.list.ListViewInflate


@LayoutView(layout = [R.layout.layout_swipe_recycler_view_inflate])
class PopupRecyclerInflate<E : Select>(val context: Context) : ListViewInflate<E, LayoutSwipeRecyclerViewInflateBinding>() {

    override fun initBinding(t: LayoutSwipeRecyclerViewInflateBinding) {
        super.initBinding(t)
        t.recyclerView.layoutManager = LinearLayoutManager(context)
    }
}