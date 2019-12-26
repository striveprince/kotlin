package com.lifecycle.binding.life.binding.data.recycler

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecycle.binding.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.life.BaseFragment
import com.lifecycle.binding.util.recyclerBinding
import com.lifecycle.binding.viewmodel.list.ListViewModel
import io.reactivex.Single


abstract class RecyclerFragment<E : Diff> : BaseFragment<ListViewModel<E>,LayoutSwipeRecyclerViewBinding>() {
    override fun createView(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): View {
        val binding = parse(t, context, parent, attachToParent)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.httpData = {offset,state-> apiData(offset, state) }
    }


    abstract fun apiData(offset:Int,state:Int): Single<List<E>>

    override fun parse(t: ListViewModel<E>, context: Context, parent: ViewGroup?, attachToParent: Boolean): LayoutSwipeRecyclerViewBinding {
        return recyclerBinding(this,t)
            .apply {
                recyclerView.apply {
                    adapter = t.adapter as RecyclerView.Adapter<*>
                    layoutManager = LinearLayoutManager(context)
                    itemAnimator = null
                }
                errorView()?.let{
                    frameLayout.removeAllViews()
                    frameLayout.addView(it)
                }
            }
    }

    open fun errorView():View? = null

    @Suppress("UNCHECKED_CAST")
    override fun initModel(): ListViewModel<E> {
        return ViewModelProviders.of(this)[ListViewModel::class.java] as ListViewModel<E>
    }
}