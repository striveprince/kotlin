package com.binding.model.adapter.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.binding.model.adapter.IModelAdapter
import com.binding.model.base.container.CycleContainer
import com.binding.model.inflate.inter.Item

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/6 16:17
 * Email: 1033144294@qq.com
 */
class FragmentAdapter<E:Item<out Fragment>> (cycleContainer: CycleContainer<*>):
    FragmentStateAdapter(cycleContainer.fragmentManager(),cycleContainer.cycle), IModelAdapter<E> {
    lateinit var recyclerView: RecyclerView
    override fun size()=holderList.size
    override val holderList=ArrayList<E>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun setIEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        return false
    }

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        notifyDataSetChanged()
        return false
    }

    override fun clear() {
        holderList.clear()
    }


    override fun getItemCount()=size()


    override fun createFragment(position: Int): Fragment {
        return holderList[position].getItem(position,recyclerView)
    }
}