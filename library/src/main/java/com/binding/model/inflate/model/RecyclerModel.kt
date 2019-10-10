package com.binding.model.inflate.model

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.binding.model.adapter.recycler.RecyclerAdapter
import com.binding.model.base.container.Container
import com.binding.model.inflate.inter.Inflate
import com.binding.model.inflate.obj.RecyclerStatus

open class RecyclerModel<T: Container,Binding: ViewDataBinding,E : Inflate<in ViewDataBinding>>(val recyclerAdapter: RecyclerAdapter<E> = RecyclerAdapter<E>())
    : ViewArrayModel<T, Binding,E,RecyclerAdapter<E>>(recyclerAdapter) {
    val layoutManagerField:ObservableField<RecyclerView.LayoutManager> = ObservableField()
    val onScrollListener  = OnScrollListener()

    override fun attachView(savedInstanceState: Bundle?, t: T) {
        super.attachView(savedInstanceState, t)
        layoutManagerField.set(LinearLayoutManager(t.dataActivity))
    }

    fun setLayoutManager(layoutManager: LinearLayoutManager){
        layoutManagerField.set(layoutManager)
    }

    inner class OnScrollListener:RecyclerView.OnScrollListener(){
        var isLast = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isLast) {
                onHttp(adapter.size(), RecyclerStatus.loadBottom)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager
            if(layoutManager is LinearLayoutManager){
                val lastVisibleItem  =  layoutManager.findLastCompletelyVisibleItemPosition()
                isLast = dy>0&&loading.get()&&(lastVisibleItem+1)>adapter.size()
//            }else if(layoutManager is StaggeredGridLayoutManager){
//                val lastVisibleItem  =  layoutManager.findLastCompletelyVisibleItemPositions()
//                isLast = dy>0&&loading.get()&&(lastVisibleItem+1)>adapter.size()
            }
        }
    }
}