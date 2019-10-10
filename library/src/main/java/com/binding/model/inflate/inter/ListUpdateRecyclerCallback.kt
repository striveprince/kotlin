package com.binding.model.inflate.inter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListUpdateCallback
import com.binding.model.adapter.recycler.RecyclerAdapter

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:55
 * Email: 1033144294@qq.com
 */
interface ListUpdateRecyclerCallback <E:Inflate<in ViewDataBinding>>:ListUpdateCallback{
    val adapter:RecyclerAdapter<E>
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position, count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
       adapter.notifyItemRangeRemoved(position, count)
    }
}