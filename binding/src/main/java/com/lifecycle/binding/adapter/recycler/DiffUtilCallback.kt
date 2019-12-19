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

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if(oldItemPosition in oldList.indices&&newItemPosition in newList.indices){
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.key() == newItem.key()
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition in oldList.indices&&newItemPosition in newList.indices&&
                newList[newItemPosition].value()==oldList[oldItemPosition].value()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if ((oldItemPosition !in oldList.indices)|| newItemPosition !in newList.indices)null
        else if(oldList[oldItemPosition].value() == newList[newItemPosition].value()) null
        else newList[newItemPosition]
    }
}