package com.binding.model.adapter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.binding.model.containsList
import com.binding.model.inflate.inter.Parse
import com.binding.model.inflate.inter.Recycler


/**
 * Created by apple on 2017/7/28.
 */

class DiffUtilCallback<E : Parse<*>>(private val oldList: List<E>, private val newList: List<E>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
       return  oldList.size
    }

    override fun getNewListSize(): Int {
        return  newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (containsList(oldItemPosition, oldList) && containsList(newItemPosition, newList)) {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem is Recycler<*> && newItem is Recycler<*>) {
                val old = (oldItem as Recycler<*>).key()
                val n = (newItem as Recycler<*>).key()
                return  n == old
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (containsList(oldItemPosition, oldList) && containsList(newItemPosition, newList)) {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return (oldItem is Recycler<*>
                    && newItem is Recycler<*>
                    && (oldItem as Recycler<*>).areContentsTheSame(newItem))
        } else
            return false
    }

}