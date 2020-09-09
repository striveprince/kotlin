package com.lifecycle.binding.adapter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.lifecycle.binding.inter.inflate.Diff


/**
 * Created by apple on 2017/7/28.
 */

class DiffUtilCallback<E : Diff>(
    private val oldList: List<E>,
    private val newList: List<E>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition in oldList.indices && newPosition in newList.indices) {
            val oldItem = oldList[oldPosition]
            val newItem = newList[newPosition]
            return oldItem.key() == newItem.key()
        }
        return false
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldPosition in oldList.indices && newPosition in newList.indices &&
                newList[newPosition].value() == oldList[oldPosition].value()
    }

    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return if ((oldPosition !in oldList.indices) || newPosition !in newList.indices || oldList[oldPosition].value() == newList[newPosition].value()) null
        else newList[newPosition]
    }
}