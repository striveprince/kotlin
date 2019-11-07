package com.binding.model.adapter.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.binding.model.containsList
import com.binding.model.inflate.inter.Diff


/**
 * Created by apple on 2017/7/28.
 */

class DiffUtilCallback<E : Diff<out ViewDataBinding>>(
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
        if (containsList(oldItemPosition, oldList) && containsList(newItemPosition, newList)) {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.key() == newItem.key()
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return containsList(oldItemPosition, oldList) &&
                containsList(newItemPosition, newList) &&
                newList[newItemPosition].value()==oldList[oldItemPosition].value()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (!containsList(oldItemPosition, oldList) || !containsList(newItemPosition, newList))return null
        else if(oldList[oldItemPosition].value() == newList[newItemPosition].value()) null
        else newList[newItemPosition]
    }
}